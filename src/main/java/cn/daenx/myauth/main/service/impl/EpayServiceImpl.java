package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.PayConfig;
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
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Result getEpay(Epay epay, MyPage myPage) {
        Page<Epay> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<Epay> epayPage = eapyMapper.selectPage(page, getQwEpayOrders(epay));
        return Result.ok("获取成功", epayPage);
    }

    /**
     * 获取查询条件构造器
     *
     * @param epay
     * @return
     */
    public LambdaQueryWrapper<Epay> getQwEpayOrders(Epay epay) {
        LambdaQueryWrapper<Epay> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epay.getId()),Epay::getId,epay.getId());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epay.getName()),Epay::getName,epay.getName());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epay.getDriver()),Epay::getDriver,epay.getDriver());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epay.getConfig()),Epay::getConfig,epay.getConfig());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epay.getContent()),Epay::getContent,epay.getContent());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epay.getUpdateTime()),Epay::getUpdateTime,epay.getUpdateTime());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epay.getEnabled()),Epay::getEnabled,epay.getEnabled());
        LambdaQueryWrapper.orderBy(true,true,Epay::getSort);
        return LambdaQueryWrapper;
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
        epayLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epay.getId()),Epay::getId,epay.getId());
        epayLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epay.getDriver()),Epay::getDriver,epay.getDriver());
        epay.setUpdateTime(Integer.valueOf(MyUtils.getTimeStamp()));
        int num = eapyMapper.update(epay, epayLambdaQueryWrapper);
        if (num == 0) {
            return Result.error("设置修改失败");
        } else {
            return Result.ok("设置修改成功");
        }
    }

    /**
     * 获取所有以开启的支付类型
     *
     * @return
     */
    @Override
    public Result getAllPayType() {
        LambdaQueryWrapper<Epay> payLambdaQueryWrapper = new LambdaQueryWrapper<>();
        payLambdaQueryWrapper.select(Epay::getId,Epay::getName,Epay::getDriver,Epay::getContent);
        payLambdaQueryWrapper.eq(Epay::getEnabled,1);
        payLambdaQueryWrapper.orderBy(true,true,Epay::getSort);
        List<Map<String, Object>> maps = eapyMapper.selectMaps(payLambdaQueryWrapper);
        return Result.ok("获取成功", maps.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList()));
    }

    /**
     * 生成充值链接
     *
     * @param payId
     * @param payDriver
     * @param money
     * @param admin
     * @return
     */
    @Override
    public Result depositMoneyLink(Integer payId , String payDriver , BigDecimal money , Admin admin) {
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId() == 0) {
            return Result.error("超级管理员无法使用此接口");
        }
        LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
        epayLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(payId), Epay::getId, payId);
        epayLambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(payDriver), Epay::getDriver, payDriver);
        Epay epay = eapyMapper.selectOne(epayLambdaQueryWrapper);
        Config config = (Config) redisUtil.get("config");
        if (CheckUtils.isObjectEmpty(config) || CheckUtils.isObjectEmpty(epay)) {
            return Result.error("设置获取失败，请检查");
        }
        if (epay.getEnabled().equals(0)){
            return Result.error("此接口以关闭");
        }
        JSONArray array = JSONUtil.parseArray(epay.getConfig());
        List<PayConfig> payConfigs = JSONUtil.toList(array, PayConfig.class);
        String pid = null;
        String url = null;
        String notify_url = null;
        String return_url = null;
        String key = null;
        String type = null;
        if (payDriver.equals("epay_wxpay")){
            type = "wxpay";
        }
        if (payDriver.equals("epay_alipay")){
            type = "alipay";
        }
        if (payDriver.equals("epay_qqpay")){
            type = "qqpay";
        }
        for (PayConfig payConfig : payConfigs) {
            if (payConfig.getFieldName().equals("url")) {
                url = payConfig.getFieldText();
            }
            if (payConfig.getFieldName().equals("pid")) {
                pid = payConfig.getFieldText();
            }
            if (payConfig.getFieldName().equals("key")) {
                key = payConfig.getFieldText();
            }
            if (payConfig.getFieldName().equals("notify_url")) {
                notify_url = payConfig.getFieldText();
            }
            if (payConfig.getFieldName().equals("return_url")) {
                return_url = payConfig.getFieldText();
            }
        }
        String out_trade_no = EpayUtil.getOrderIdByTime(admin.getId());//生成订单号
        HashMap<String, String> map = new HashMap<>();
        map.put("pid", pid);//pid
        map.put("type", type);//支付方式,alipay:支付宝,wxpay:微信支付,qqpay:QQ钱包
        map.put("out_trade_no", out_trade_no);//订单号,自己生成
        map.put("notify_url", MyUtils.removeDH(notify_url) + "/myauth/web/epayNotify");//服务器异步通知地址
        map.put("return_url", return_url);//页面跳转通知地址
        map.put("name", admin.getUser() + "余额充值");//商品名称
        map.put("money", String.valueOf(money));//金额
        map = (HashMap<String, String>) EpayUtil.sortByKey(map);
        String signStr = "";
        //遍历map 转成字符串
        for (Map.Entry<String, String> m : map.entrySet()) {
            //不拼接值为空的字段
            if (!CheckUtils.isObjectEmpty(m.getValue())) {
                signStr += m.getKey() + "=" + m.getValue() + "&";
            }
        }
        //去掉最后一个 &
        signStr = signStr.substring(0, signStr.length() - 1);
        //保存一次提交数据
        String s = signStr;
        //最后加上key等待MD5
        signStr += key;
        //转为MD5
        signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
        //跳转支付的url地址
        String s1 = url + "/submit.php?" + s + "&sign=" + signStr + "&sign_type=MD5";
        EpayOrders epayOrders = new EpayOrders();
        epayOrders.setOutTradeNo(out_trade_no);
        epayOrders.setType(epay.getDriver());
        epayOrders.setAddtime(MyUtils.dateToStr(MyUtils.stamp2Date(MyUtils.getTimeStamp())));
        epayOrders.setName(admin.getUser() + "余额充值");
        epayOrders.setMoney(String.valueOf(money));
        epayOrders.setStatus(0);
        epayOrders.setFromAdminId(admin.getId());
        epayOrdersMapper.insert(epayOrders);
        return Result.ok(s1, epayOrders);
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
            EpayOrders epayOrders = epayOrdersMapper.selectOne(LambdaQueryWrapper);
            if (CheckUtils.isObjectEmpty(epayOrders)){
                return Result.error("商户订单号匹配失败");
            }
            if(epayOrders.getStatus().equals(1)){
                return Result.error("订单已成功加款，请勿重复通知");
            }
            LambdaQueryWrapper<Epay> epayLambdaQueryWrapper = new LambdaQueryWrapper<>();
            epayLambdaQueryWrapper.eq(type.equals("wxpay"),Epay::getDriver,"epay_wxpay");
            epayLambdaQueryWrapper.eq(type.equals("alipay"),Epay::getDriver,"epay_alipay");
            epayLambdaQueryWrapper.eq(type.equals("qqpay"),Epay::getDriver,"epay_qqpay");
            Epay epay = eapyMapper.selectOne(epayLambdaQueryWrapper);
            JSONArray array = JSONUtil.parseArray(epay.getConfig());
            List<PayConfig> payConfigs = JSONUtil.toList(array, PayConfig.class);
            String ePid = null;
            String url = null;
            String key = null;
            for (PayConfig payConfig : payConfigs) {
                if (payConfig.getFieldName().equals("url")) {
                    url = payConfig.getFieldText();
                }
                if (payConfig.getFieldName().equals("pid")) {
                    ePid = payConfig.getFieldText();
                }
                if (payConfig.getFieldName().equals("key")) {
                    key = payConfig.getFieldText();
                }
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("pid",String.valueOf(pid));//pid
            map.put("trade_no",trade_no);//易支付订单号
            map.put("out_trade_no",out_trade_no);//商户订单号,自己生成
            map.put("type",type);//支付方式,alipay:支付宝,wxpay:微信支付,qqpay:QQ钱包
            map.put("name",name);//商品名称
            map.put("money",money);//商品金额
            map.put("trade_status",trade_status);//订单状态
            map = (HashMap<String, String>) EpayUtil.sortByKey(map);
            String signStr = "";
            //遍历map 转成字符串
            for(Map.Entry<String,String> m :map.entrySet()){
                //不拼接值为空的字段
                if(!CheckUtils.isObjectEmpty(m.getValue())){
                    signStr += m.getKey() + "=" +m.getValue()+"&";
                }
            }
            //去掉最后一个 &
            signStr = signStr.substring(0,signStr.length()-1);
            //最后加上key等待MD5
            signStr += key;
            //转为MD5
            signStr = DigestUtils.md5DigestAsHex(signStr.getBytes());
            if (signStr.equals(sign) && sign_type.equals("MD5")){
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("act", "order");
                paramMap.put("pid", ePid);
                paramMap.put("key", key);
                paramMap.put("out_trade_no", out_trade_no);
                String result = HttpUtil.get(MyUtils.removeDH(url) + "/api.php", paramMap);
                EpayOrders epayOrders1 = JSONUtil.toBean(result, EpayOrders.class);
                if(CheckUtils.isObjectEmpty(epayOrders1) || epayOrders1.getStatus().equals(0)){
                    return Result.error("订单接收通知成功,异步验证订单失败");
                }
                Admin admin = adminMapper.selectById(epayOrders.getFromAdminId());
                BigDecimal oldMoney = new BigDecimal(admin.getMoney());
                BigDecimal addMoney = new BigDecimal(epayOrders1.getMoney());
                String nowMoney = String.valueOf(oldMoney.add(addMoney));
                admin.setMoney(nowMoney);
                epayOrders.setStatus(epayOrders1.getStatus());
                epayOrders.setAddtime(epayOrders1.getAddtime());
                epayOrders.setEndtime(epayOrders1.getEndtime());
                epayOrders.setTradeNo(epayOrders1.getTradeNo());
                Alog alog = new Alog();
                alog.setMoney(epayOrders1.getMoney());
                alog.setAfterMoney(nowMoney);
                alog.setAdminId(admin.getId());
                alog.setData("后台充值：" + "支付方式" + epay.getDriver() + ",充值金额" + epayOrders1.getMoney());
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
