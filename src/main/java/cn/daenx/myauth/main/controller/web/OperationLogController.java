package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.OperationLog;
import cn.daenx.myauth.main.service.IOperationLogService;
import cn.daenx.myauth.util.CheckUtils;
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
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class OperationLogController {
    @Resource
    private IOperationLogService operationLogService;

    /**
     * 获取操作日志列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getOperationLogList")
    public Result getOperationLogList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        OperationLog operationLog = jsonObject.toJavaObject(OperationLog.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(operationLog) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return operationLogService.getOperationLogList(operationLog, myPage);
    }
}
