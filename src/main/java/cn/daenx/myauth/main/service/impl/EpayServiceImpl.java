package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.mapper.AdminMapper;
import cn.daenx.myauth.main.mapper.AlogMapper;
import cn.daenx.myauth.main.mapper.EpayMapper;
import cn.daenx.myauth.main.mapper.EpayOrdersMapper;
import cn.daenx.myauth.main.service.IEpayService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.EpayUtil;
import cn.daenx.myauth.util.MyUtils;
import cn.daenx.myauth.util.RedisUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @since 2022-05-13
 */
@Service
public class EpayServiceImpl extends ServiceImpl<EpayMapper, Epay> implements IEpayService {
    @Resource
    private EpayMapper eapyMapper;
    @Resource
    private AdminMapper adminMapper;
    @Resource
    private EpayOrdersMapper epayOrdersMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AlogMapper alogMapper;

    /**
     * 获取epay配置
     *
     * @return
     */
    @Override
    public Result getEpay() {
        LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Epay> eapyList = eapyMapper.selectList(epayLambdaQueryWrapper);
        if (eapyList.size() == 0) {
            return Result.error("设置获取失败或者没有设置,请直接新增");
        } else {
            return Result.ok("获取成功", eapyList.get(0));
        }
    }

    /**
     * 修改epay配置
     *
     * @param epay
     * @return
     */
    @Override
    public Result editEpay(Epay epay) {
        LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Epay> eapyList = eapyMapper.selectList(epayLambdaQueryWrapper);
        if (eapyList.size() == 0) {
            int num = eapyMapper.insert(epay);
            if (num == 0) {
                return Result.error("设置修改失败");
            } else {
                return Result.ok("设置修改成功");
            }
        } else {
            epay.setId(null);
            LambdaQueryWrapper<Epay> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Epay::getId, eapyList.get(0).getId());
            int num = eapyMapper.update(epay, queryWrapper);
            if (num == 0) {
                return Result.error("设置修改失败");
            } else {
                return Result.ok("设置修改成功");
            }
        }
    }

    /**
     * 生成epay下单链接
     *
     * @param money
     * @param type
     * @param admin
     * @return
     */
    @Override
    public Result depositMoneyLink(BigDecimal money , String type , Admin admin)  {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Epay> eapyList = eapyMapper.selectList(epayLambdaQueryWrapper);
        Config config = (Config) redisUtil.get("config");
        if (CheckUtils.isObjectEmpty(config) || CheckUtils.isObjectEmpty(eapyList)) {
            return Result.error("设置获取失败，请检查");
        }
        String webSite = MyUtils.removeDH(eapyList.get(0).getUrl());//易支付网站地址
        String out_trade_no = EpayUtil.getOrderIdByTime(admin.getId());//生成订单号
        HashMap<String, String> map = new HashMap<>();
        map.put("pid",String.valueOf(eapyList.get(0).getPid()));//pid
        map.put("type",type);//支付方式,alipay:支付宝,wxpay:微信支付,qqpay:QQ钱包
        map.put("out_trade_no",out_trade_no);//订单号,自己生成
        map.put("notify_url",MyUtils.removeDH(eapyList.get(0).getNotifyUrl()) + "/myauth/web/epayNotify");//服务器异步通知地址
        map.put("return_url",eapyList.get(0).getReturnUrl());//页面跳转通知地址
        map.put("name",admin.getUser() + "余额充值");//商品名称
        map.put("money",String.valueOf(money));//金额
        map.put("sitename",config.getSeoTitle());//网站名称
        String key = eapyList.get(0).getEkey();//易支付秘钥
        map = (HashMap<String, String>) EpayUtil.sortByKey(map);
        String signStr = "";
        //遍历map 转成字符串
        for(Map.Entry<String,String> m :map.entrySet()){
            signStr += m.getKey() + "=" +m.getValue()+"&";
        }
        //去掉最后一个 &
        signStr = signStr.substring(0,signStr.length()-1);
        //保存一次提交数据
        String s = signStr;
        //最后加上key等待MD5
        signStr += key;
        //转为MD5
        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
        //跳转支付的url地址
        String s1 = webSite + "/submit.php?" + s + "&sign=" + signStr + "&sign_type=MD5";
        EpayOrders epayOrders = new EpayOrders();
        epayOrders.setOutTradeNo(out_trade_no);
        epayOrders.setType(type);
        epayOrders.setAddtime(MyUtils.dateToStr(MyUtils.stamp2Date(MyUtils.getTimeStamp())));
        epayOrders.setName(admin.getUser() + "余额充值");
        epayOrders.setMoney(String.valueOf(money));
        epayOrders.setStatus(0);
        epayOrders.setFromAdminId(admin.getId());
        epayOrdersMapper.insert(epayOrders);
        return Result.ok(s1,epayOrders);
    }

    /**
     * 处理epay异步通知
     *
     * @param pid
     * @param trade_no
     * @param out_trade_no
     * @param type
     * @param name
     * @param money
     * @param trade_status
     * @param sign
     * @param sign_type
     */
    @Override
    public Result epayNotify(Integer pid ,
                             String trade_no ,
                             String out_trade_no ,
                             String type ,
                             String name ,
                             String money ,
                             String trade_status ,
                             String sign ,
                             String sign_type) {


        if(trade_status.equals("TRADE_SUCCESS")){
            LambdaQueryWrapper<EpayOrders> LambdaQueryWrapper = new LambdaQueryWrapper<>();
            LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(out_trade_no), EpayOrders::getOutTradeNo, out_trade_no);
            LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(type), EpayOrders::getType, type);
            EpayOrders epayOrders = epayOrdersMapper.selectOne(LambdaQueryWrapper);
            if (CheckUtils.isObjectEmpty(epayOrders)){
                return Result.error("商户订单号匹配失败");
            }
            LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            List<Epay> eapyList = eapyMapper.selectList(epayLambdaQueryWrapper);
            HashMap<String, String> map = new HashMap<>();
            map.put("pid",String.valueOf(pid));//pid
            map.put("trade_no",trade_no);//易支付订单号
            map.put("out_trade_no",out_trade_no);//商户订单号,自己生成
            map.put("type",type);//支付方式,alipay:支付宝,wxpay:微信支付,qqpay:QQ钱包
            map.put("name",name);//商品名称
            map.put("money",money);//商品金额
            map.put("trade_status",trade_status);//订单状态
            String key = eapyList.get(0).getEkey();//易支付秘钥
            map = (HashMap<String, String>) EpayUtil.sortByKey(map);
            String signStr = "";
            //遍历map 转成字符串
            for(Map.Entry<String,String> m :map.entrySet()){
                signStr += m.getKey() + "=" +m.getValue()+"&";
            }
            //去掉最后一个 &
            signStr = signStr.substring(0,signStr.length()-1);
            //最后加上key等待MD5
            signStr += key;
            //转为MD5
            signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
            if (signStr.equals(sign) && sign_type.equals("MD5")){
                Admin admin = adminMapper.selectById(epayOrders.getFromAdminId());
                BigDecimal oldMoney = new BigDecimal(admin.getMoney());
                BigDecimal addMoney = new BigDecimal(money);
                String nowMoney = String.valueOf(oldMoney.add(addMoney));
                admin.setMoney(nowMoney);
                epayOrders.setStatus(1);
                epayOrders.setEndtime(MyUtils.dateToStr(MyUtils.stamp2Date(MyUtils.getTimeStamp())));
                epayOrders.setTradeNo(trade_no);
                Alog alog = new Alog();
                alog.setMoney(money);
                alog.setAfterMoney(nowMoney);
                alog.setAdminId(admin.getId());
                alog.setData("后台充值：" + "支付方式" + type + ",充值金额" + money);
                alog.setType("后台充值");
                alog.setAddTime(Integer.valueOf(MyUtils.getTimeStamp()));
                epayOrdersMapper.updateById(epayOrders);
                adminMapper.updateById(admin);
                alogMapper.insert(alog);
                return Result.ok("加款成功");
            }
            return Result.error("验签失败");
        }
        return Result.error("加款失败");
    }
}
