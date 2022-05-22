package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.MailSend;
import cn.daenx.myauth.main.mapper.MailSendMapper;
import cn.daenx.myauth.main.service.IEmailService;
import cn.daenx.myauth.main.service.IMailSendService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @since 2022-05-16 20:24
 */
@Service
public class MailSendServiceImpl extends ServiceImpl<MailSendMapper, MailSend> implements IMailSendService {

    @Resource
    private MailSendMapper mailSendMapper;
    @Resource
    private IEmailService emailService;

    /**
     * 按ID或者发送类型获取发送配置
     *
     * @param mailSend
     * @return
     */
    @Override
    public Result getMailSend(MailSend mailSend) {
        LambdaQueryWrapper<MailSend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(mailSend.getId()), MailSend::getId, mailSend.getId());
        lambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(mailSend.getSendType()), MailSend::getSendType, mailSend.getSendType());
        MailSend mailSend1 =  mailSendMapper.selectOne(lambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(mailSend1)){
            return Result.ok("查询成功",mailSend1);
        }
        return Result.error("查询失败");
    }

    /**
     * 修改发送配置
     *
     * @param mailSend
     * @return
     */
    @Override
    public Result updMailSend(MailSend mailSend) {
        LambdaQueryWrapper<MailSend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(mailSend.getId()), MailSend::getId, mailSend.getId());
        lambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(mailSend.getSendType()), MailSend::getSendType, mailSend.getSendType());
        int num = mailSendMapper.update(mailSend, lambdaQueryWrapper);
        if (num == 0) {
            return Result.error("设置修改失败");
        } else {
            return Result.ok("设置修改成功");
        }
    }

    /**
     * 获取全部发送类型
     *
     * @return
     */
    @Override
    public Result getSendTypeList() {
        LambdaQueryWrapper<MailSend> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(MailSend::getId, MailSend::getSendType);
        List<Map<String, Object>> maps = mailSendMapper.selectMaps(lambdaQueryWrapper);
        return Result.ok("获取成功", maps.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList()));
    }

    /**
     * 获取所有发送配置
     *
     * @return
     */
    @Override
    public Result getMailSendList(MailSend mailSend, MyPage myPage) {
        Page<MailSend> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<MailSend> mailSendPage = mailSendMapper.selectPage(page, getQwMailSend(mailSend));
        return Result.ok("获取成功", mailSendPage);
    }

    /**
     * 发送邮件测试
     *
     * @param sendMail
     * @param theme
     * @param txt
     * @return
     */
    @Override
    public Result sendMailTest(String sendMail, String theme, String txt) {
        try {
            emailService.sendFullTextEmail(theme, txt, new String[]{sendMail});
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("发送失败、请检查邮箱系统配置。");
        }
        return Result.ok("发送成功");
    }

    /**
     * 开启发送，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result openSendMail(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<MailSend> wrapper = new LambdaUpdateWrapper();
        wrapper.set(MailSend::getSendSwitch, 1).in(MailSend::getId, Arrays.asList(idArray)).eq(MailSend::getSendSwitch, 0);
        int okCount = mailSendMapper.update(null, wrapper);
        return Result.ok("成功开启 " + okCount + " 项通知");
    }

    /**
     * 关闭发送，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result turnOffSend(String ids) {
        String[] idArray = ids.split(",");
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        LambdaUpdateWrapper<MailSend> wrapper = new LambdaUpdateWrapper();
        wrapper.set(MailSend::getSendSwitch, 0).in(MailSend::getId, Arrays.asList(idArray)).eq(MailSend::getSendSwitch, 1);
        int okCount = mailSendMapper.update(null, wrapper);
        return Result.ok("成功关闭 " + okCount + " 项通知");
    }

    /**
     * 获取查询条件构造器
     *
     * @param mailSend
     * @return
     */
    public LambdaQueryWrapper<MailSend> getQwMailSend(MailSend mailSend) {
        LambdaQueryWrapper<MailSend> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(mailSend.getSendType()), MailSend::getSendType, mailSend.getSendType());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(mailSend.getSendSwitch()), MailSend::getSendSwitch, mailSend.getSendSwitch());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(mailSend.getSendTheme()), MailSend::getSendTheme, mailSend.getSendTheme());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(mailSend.getSendTitle()), MailSend::getSendTitle, mailSend.getSendTitle());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(mailSend.getSendTemplates()), MailSend::getSendTemplates, mailSend.getSendTemplates());
        return LambdaQueryWrapper;
    }
}
