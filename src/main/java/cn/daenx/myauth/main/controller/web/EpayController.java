package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Epay;
import cn.daenx.myauth.main.service.IEpayService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 前端web使用的API接口
 *
 * @author
 * @since 2022-05-13
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class EpayController {
    @Resource
    private IEpayService epayService;


    /**
     * 修改epay设置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("editEpay")
    public Result editEpay(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Epay epay = jsonObject.toJavaObject(Epay.class);
        if (CheckUtils.isObjectEmpty(epay)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(epay.getUrl()) &&
                CheckUtils.isObjectEmpty(epay.getPid()) &&
                CheckUtils.isObjectEmpty(epay.getEkey()) &&
                CheckUtils.isObjectEmpty(epay.getNotifyUrl()) &&
                CheckUtils.isObjectEmpty(epay.getReturnUrl()))
        {
            return Result.error("全部参数均不能为空");
        }
        return epayService.editEpay(epay);
    }


    /**
     * 获取epay设置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @GetMapping("getEpay")
    public Result getEpay(HttpServletRequest request) {
        return epayService.getEpay();
    }



    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("depositMoneyLink")
    public Result depositMoneyLink(HttpServletRequest request){
        Admin admin = (Admin) request.getAttribute("obj_admin");
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        BigDecimal money = jsonObject.getBigDecimal("money");
        String type = jsonObject.getString("type");
        if(CheckUtils.isObjectEmpty(money) && CheckUtils.isObjectEmpty(type)){
            return Result.error("参数错误");
        }
        return epayService.depositMoneyLink(money,type,admin);
    }


    @NoEncryptNoSign
    @GetMapping("epayNotify")
    public Result epayNotify(Integer pid ,
                           String trade_no ,
                           String out_trade_no ,
                           String type ,
                           String name ,
                           String money ,
                           String trade_status ,
                           String sign ,
                           String sign_type){

       return epayService.epayNotify(pid,trade_no,out_trade_no,type,name,money,trade_status,sign,sign_type);

    }
}
