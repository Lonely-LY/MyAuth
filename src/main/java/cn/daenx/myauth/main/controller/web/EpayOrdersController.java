package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.entity.EpayOrders;
import cn.daenx.myauth.main.service.IEpayOrdersService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 前端web使用的API接口
 *
 * @author
 * @since 2022-05-13
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class EpayOrdersController {
    @Resource
    private IEpayOrdersService epayOrdersService;


    /**
     * 获取epay订单列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getEpayOrdersList")
    public Result getEpayOrdersList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        EpayOrders epayOrders = jsonObject.toJavaObject(EpayOrders.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return epayOrdersService.getEpayOrdersList(epayOrders,myPage);
    }


    /**
     * 查询epay订单
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("getEpayOrder")
    public Result getEpayOrder(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String outTradeNo = jsonObject.getString("outTradeNo");
        if (CheckUtils.isObjectEmpty(outTradeNo)){
            return Result.error("outTradeNo不能为空");
        }
        return epayOrdersService.getEpayOrder(outTradeNo);
    }


    /**
     * 删除epay订单（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delEpayOrders")
    public Result delEpayOrders(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return epayOrdersService.delEpayOrders(ids);
    }


}
