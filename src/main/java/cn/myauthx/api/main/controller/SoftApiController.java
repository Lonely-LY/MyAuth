package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.*;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 软件使用API接口
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/soft")
public class SoftApiController {
    @Resource
    private IVersionService versionService;

    /**
     * 初始化软件
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @PostMapping("/init")
    public Result init(HttpServletRequest request) {
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        JSONObject retJson = new JSONObject(true);
        retJson.put("Name",soft.getName());
        retJson.put("Status",soft.getStatus());
        retJson.put("Type",soft.getType());
        retJson.put("BatchSoft",soft.getBatchSoft());
        retJson.put("MultipleLogin",soft.getMultipleLogin());
        retJson.put("HeartTime",soft.getHeartTime());
        return Result.ok("初始化成功",retJson);
    }
    @SoftValidated
    @DataDecrypt
    @SignValidated
    @PostMapping("/checkUpdate")
    public Result checkUpdate(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        String vkey = jsonObject.getString("vkey");
        if(CheckUtils.isObjectEmpty(vkey)){
            return Result.error("缺少vkey参数");
        }
        Version version = new Version();
        version.setVkey(vkey);
        version.setFromSoftId(soft.getId());
        return versionService.checkUpdate(version,soft);
    }
}