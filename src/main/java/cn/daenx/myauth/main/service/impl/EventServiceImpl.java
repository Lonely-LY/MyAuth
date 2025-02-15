package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.main.entity.Event;
import cn.daenx.myauth.main.entity.Plog;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.User;
import cn.daenx.myauth.main.mapper.EventMapper;
import cn.daenx.myauth.main.mapper.PlogMapper;
import cn.daenx.myauth.main.mapper.SoftMapper;
import cn.daenx.myauth.main.mapper.UserMapper;
import cn.daenx.myauth.main.service.IEventService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.enums.EventEnums;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-06
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements IEventService {
    @Resource
    private EventMapper eventMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PlogMapper plogMapper;
    @Resource
    private SoftMapper softMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 触发事件
     *
     * @param name
     * @param user
     * @param soft
     * @return
     */
    @Override
    public Result letEvent(String name, User user, Soft soft, String token) {
        Plog plog = new Plog();
        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eventLambdaQueryWrapper.eq(Event::getName, name);
        eventLambdaQueryWrapper.eq(Event::getFromSoftId, soft.getId());
        Event event = eventMapper.selectOne(eventLambdaQueryWrapper);
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("事件name错误或者不存在");
        }
        if (event.getStatus().equals(EventEnums.STATUS_DISABLE.getCode())) {
            return Result.error("事件已被禁用");
        }
        if(!event.getStartTime().equals(0) && Integer.parseInt(MyUtils.getTimeStamp()) < event.getStartTime()){
            return Result.error("事件还未到使用日期");
        }
        if(!event.getEndTime().equals(0) && Integer.parseInt(MyUtils.getTimeStamp()) > event.getEndTime()){
            return Result.error("事件已超过使用日期");
        }

        LambdaQueryWrapper<Plog> plogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        plogLambdaQueryWrapper.eq(Plog::getFromUser, user.getUser());
        plogLambdaQueryWrapper.eq(Plog::getFromEventName, event.getName());
        Long count =  plogMapper.selectCount(plogLambdaQueryWrapper);
        if(!event.getAllCount().equals(0) && count >= event.getAllCount()){
            return Result.error("事件已超过总共可使用次数");
        }
        //获取当天开始时间
        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);//当天零点
        //获取当天结束时间
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);//当天24点
        //获取秒数  10位
        long start_time = today_start.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        long end_time = today_end.toInstant(ZoneOffset.ofHours(8)).getEpochSecond();
        plogLambdaQueryWrapper.ge(Plog::getAddTime, start_time);
        plogLambdaQueryWrapper.le(Plog::getAddTime, end_time);
        Long count1 =  plogMapper.selectCount(plogLambdaQueryWrapper);
        if(!event.getDayCount().equals(0) && count1 >= event.getDayCount()){
            return Result.error("事件已超过当日可使用次数");
        }
        plog.setPoint(event.getPoint());
        if (event.getPoint() != 0) {
            if (event.getPoint() > 0) {
                user.setPoint(user.getPoint() + event.getPoint());
            } else {
                Integer af = user.getPoint() + event.getPoint();
                if (af < 0) {
                    return Result.error("点数不足，至少需要" + -event.getPoint() + "点");
                } else {
                    user.setPoint(af);
                }
            }
        }
        plog.setAfterPoint(user.getPoint());
        plog.setSeconds(event.getSeconds());
        if (event.getSeconds() != 0) {
            if (event.getSeconds() > 0) {
                user.setAuthTime(user.getAuthTime() + event.getSeconds());
            } else {
                Integer af = user.getAuthTime() + event.getSeconds();
                if (af < Integer.parseInt(MyUtils.getTimeStamp())) {
                    return Result.error("授权期限不足");
                } else {
                    user.setAuthTime(af);
                }
            }
        }
        plog.setAfterSeconds(user.getAuthTime());
        plog.setFromUser(user.getUser());
        plog.setAddTime(Integer.parseInt(MyUtils.getTimeStamp()));
        plog.setFromEventName(event.getName());
        plog.setFromEventId(event.getId());
        plog.setFromSoftId(event.getFromSoftId());
        int num = userMapper.updateById(user);
        if (num > 0) {
            //redisUtil.set("user:" + user.getFromSoftId() + ":" + user.getUser() + ":" + token, user, soft.getHeartTime());
            Set<String> scan = redisUtil.scan("user:" + user.getFromSoftId() + ":" + user.getUser() + ":*");
            if(scan.size() > 0){
                for (String s : scan) {
                    redisUtil.set(s, user, soft.getHeartTime());
                }
            }
            plogMapper.insert(plog);
            JSONObject jsonObject = new JSONObject(true);
            jsonObject.put("user", user.getUser());
            jsonObject.put("name", user.getName());
            jsonObject.put("qq", user.getQq());
            jsonObject.put("point", user.getPoint());
            jsonObject.put("ckey", user.getCkey());
            jsonObject.put("regTime", user.getRegTime());
            jsonObject.put("remark", user.getRemark());
            jsonObject.put("authTime", user.getAuthTime());
            return Result.ok("触发事件成功", jsonObject);
        } else {
            return Result.error("触发事件失败");
        }
    }

    /**
     * 获取查询条件构造器
     *
     * @param event
     * @return
     */
    public LambdaQueryWrapper<Event> getQwEvent(Event event) {
        LambdaQueryWrapper<Event> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(event.getName()), Event::getName, event.getName());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(event.getAddTime()), Event::getAddTime, event.getAddTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(event.getStatus()), Event::getStatus, event.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(event.getFromSoftId()), Event::getFromSoftId, event.getFromSoftId());
        return LambdaQueryWrapper;
    }

    /**
     * 获取事件列表
     *
     * @param event
     * @param myPage
     * @return
     */
    @Override
    public Result getEventList(Event event, MyPage myPage) {
        Page<Event> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Event> msgPage = eventMapper.selectPage(page, getQwEvent(event));
        for (int i = 0; i < msgPage.getRecords().size(); i++) {
            Soft obj = (Soft) redisUtil.get("id:soft:" + msgPage.getRecords().get(i).getFromSoftId());
            msgPage.getRecords().get(i).setFromSoftName(obj.getName());
        }
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 查询事件
     *
     * @param event
     * @return
     */
    @Override
    public Result getEvent(Event event) {
        Event newEvent = eventMapper.selectById(event.getId());
        if (CheckUtils.isObjectEmpty(newEvent)) {
            return Result.error("查询失败，未找到");
        }
        return Result.ok("查询成功", newEvent);
    }

    /**
     * 修改事件
     *
     * @param event
     * @return
     */
    @Override
    public Result updEvent(Event event) {
        Event event1 = eventMapper.selectById(event.getId());
        if (CheckUtils.isObjectEmpty(event1)) {
            return Result.error("事件ID错误");
        }
        if (!event1.getName().equals(event.getName())) {
            LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
            eventLambdaQueryWrapper.eq(Event::getFromSoftId, event1.getFromSoftId());
            eventLambdaQueryWrapper.eq(Event::getName, event.getName());
            List<Event> eventList = eventMapper.selectList(eventLambdaQueryWrapper);
            if (eventList.size() > 0) {
                return Result.error("事件名称在当前软件中已存在");
            }
        }
        event.setFromSoftId(null);
        int num = eventMapper.updateById(event);
        if (num <= 0) {
            return Result.error("修改失败");
        }
        return Result.ok("修改成功");
    }

    /**
     * 添加事件
     *
     * @param eventC
     * @return
     */
    @Override
    public Result addEvent(Event eventC) {
        Soft soft = softMapper.selectById(eventC.getFromSoftId());
        if (CheckUtils.isObjectEmpty(soft)) {
            return Result.error("fromSoftId错误");
        }
        LambdaQueryWrapper<Event> eventLambdaQueryWrapper = new LambdaQueryWrapper<>();
        eventLambdaQueryWrapper.eq(Event::getFromSoftId, eventC.getFromSoftId());
        eventLambdaQueryWrapper.eq(Event::getName, eventC.getName());
        Event event = eventMapper.selectOne(eventLambdaQueryWrapper);
        if (!CheckUtils.isObjectEmpty(event)) {
            return Result.error("事件名称在当前软件中已存在");
        }
        eventC.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = eventMapper.insert(eventC);
        if (num <= 0) {
            return Result.error("添加失败");
        }
        return Result.ok("添加成功");
    }

    /**
     * 删除事件
     *
     * @param eventC
     * @return
     */
    @Override
    public Result delEvent(Event eventC) {
        Event event = eventMapper.selectById(eventC.getId());
        if (CheckUtils.isObjectEmpty(event)) {
            return Result.error("删除失败，id错误");
        }
        int num = eventMapper.deleteById(eventC.getId());
        if (num <= 0) {
            return Result.error("删除失败");
        }
        return Result.ok("删除成功");
    }
}
