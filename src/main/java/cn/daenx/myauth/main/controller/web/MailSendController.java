package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.MailSend;
import cn.daenx.myauth.main.service.IMailSendService;
import cn.daenx.myauth.util.CheckUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @since 2022-05-16 21:57
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class MailSendController {
    @Resource
    private IMailSendService mailSendService;

    /**
     * 按ID或者发送类型获取发送配置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getMailSend")
    public Result getMailSend(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        MailSend mailSend = jsonObject.toJavaObject(MailSend.class);
        if(CheckUtils.isObjectEmpty(mailSend.getId()) && CheckUtils.isObjectEmpty(mailSend.getSendType())){
            return Result.error("id和sendType不能都为空");
        }
        return mailSendService.getMailSend(mailSend);
    }

    /**
     * 修改发送配置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("updMailSend")
    public Result updMailSend(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        MailSend mailSend = jsonObject.toJavaObject(MailSend.class);
        if(CheckUtils.isObjectEmpty(mailSend.getId()) && CheckUtils.isObjectEmpty(mailSend.getSendType())){
            return Result.error("id和sendType不能都为空");
        }
        return mailSendService.updMailSend(mailSend);
    }

    /**
     * 获取全部发送类型
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @GetMapping("getSendTypeList")
    public Result getSendTypeList(HttpServletRequest request) {
        return mailSendService.getSendTypeList();
    }

    /**
     * 获取所有发送配置
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getMailSendList")
    public Result getMailSendList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        MailSend mailSend = jsonObject.toJavaObject(MailSend.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return mailSendService.getMailSendList(mailSend, myPage);
    }

    /**
     * 发送邮件测试
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("sendMailTest")
    public Result sendMailTest(HttpServletRequest request){
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String sendMail = jsonObject.getString("sendMail");
        String theme = jsonObject.getString("theme");
        String txt = jsonObject.getString("txt");
        if (CheckUtils.isObjectEmpty(sendMail) || CheckUtils.isObjectEmpty(theme) || CheckUtils.isObjectEmpty(txt)) {
            return Result.error("全部参数均不能为空");
        }
        return mailSendService.sendMailTest(sendMail, theme, txt);
    }

}
