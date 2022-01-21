package cn.myauthx.api.main.controller;

import cn.myauthx.api.base.annotation.*;
import cn.myauthx.api.base.vo.Result;
import cn.myauthx.api.main.entity.Soft;
import cn.myauthx.api.main.entity.User;
import cn.myauthx.api.main.entity.Version;
import cn.myauthx.api.main.enums.SoftEnums;
import cn.myauthx.api.main.service.IUserService;
import cn.myauthx.api.main.service.IVersionService;
import cn.myauthx.api.util.CheckUtils;
import cn.myauthx.api.util.IpUtil;
import cn.myauthx.api.util.MyUtils;
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
    @Resource
    private IUserService userService;
    /**
     * 初始化软件
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
    @PostMapping("/init")
    public Result init(HttpServletRequest request) {
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        JSONObject retJson = new JSONObject(true);
        retJson.put("Name",soft.getName());
        retJson.put("Status",soft.getStatus());
        retJson.put("Type",soft.getType());
        retJson.put("BindDeviceCode",soft.getBindDeviceCode());
        retJson.put("HeartTime",soft.getHeartTime());
        return Result.ok("初始化成功",retJson);
    }

    /**
     * 检测更新
     * @param request
     * @return
     */
    @SoftValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
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

    /**
     * 注册
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
    @PostMapping("/register")
    public Result register(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        User user = jsonObject.getJSONObject("data").toJavaObject(User.class);
        if(CheckUtils.isObjectEmpty(user)){
            return Result.error("参数错误");
        }
        if(soft.getRegister().equals(SoftEnums.REGISTER_DISABLE.getCode())){
            return Result.error("当前不允许注册新用户");
        }
        if(CheckUtils.isObjectEmpty(user.getUser())){
            return Result.error("账号不能为空");
        }
        String ip = IpUtil.getIpAddr(request);
        user.setLastIp(ip);
        return userService.register(user,soft);
    }

    /**
     * 登录
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
    @PostMapping("login")
    public Result login(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        Version version = (Version) request.getAttribute("obj_version");
        User user = jsonObject.getJSONObject("data").toJavaObject(User.class);
        String ip = IpUtil.getIpAddr(request);
        user.setLastIp(ip);
        user.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        user.setFromVerId(version.getId());
        user.setFromVerKey(version.getVkey());
        return userService.login(user,soft);
    }

    /**
     * 心跳
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @UserLogin
    @BanValidated(is_ip = true,is_device_code = true,is_user = true)
    @PostMapping("heart")
    public Result heart(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        User user = (User) request.getAttribute("obj_user");
        user.setLastTime(Integer.valueOf(MyUtils.getTimeStamp()));
        return userService.heart(user,soft);
    }

    /**
     * 使用卡密
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
    @PostMapping("/useCkey")
    public Result useCkey(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        User user = jsonObject.getJSONObject("data").toJavaObject(User.class);
        if(CheckUtils.isObjectEmpty(user)){
            return Result.error("参数错误");
        }
        if(CheckUtils.isObjectEmpty(user.getUser())){
            return Result.error("账号不能为空");
        }
        if(CheckUtils.isObjectEmpty(user.getCkey())){
            return Result.error("卡密不能为空");
        }
        return userService.useCkey(user,soft);
    }

    /**
     * 获取回复
     * @param request
     * @return
     */
    @SoftValidated
    @VersionValidated
    @DataDecrypt
    @SignValidated
    @BanValidated(is_ip = true,is_device_code = true,is_user = false)
    @PostMapping("/getMsg")
    public Result getMsg(HttpServletRequest request){
        //不管有没有加密和解密，取提交的JSON都要通过下面这行去取
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Soft soft = (Soft) request.getAttribute("obj_soft");
        Version version = (Version) request.getAttribute("obj_version");
        String keyword = jsonObject.getJSONObject("data").getString("keyword");
        if(CheckUtils.isObjectEmpty(keyword)){
            return Result.error("keyword不能为空");
        }
        return userService.getMsg(soft,version,keyword);
    }
}
