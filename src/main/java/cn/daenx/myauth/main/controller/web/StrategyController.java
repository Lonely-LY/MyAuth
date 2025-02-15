package cn.daenx.myauth.main.controller.web;


import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Role;
import cn.daenx.myauth.main.entity.Strategy;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.RedisUtil;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.*;
import cn.daenx.myauth.main.service.IStrategyService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author DaenMax
 * @since 2022-03-09
 */
@RestController
@RequestMapping("/web")
public class StrategyController {
    @Resource
    private IStrategyService strategyService;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取策略列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getStrategyList")
    public Result getStrategyList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(strategy) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(strategy.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return strategyService.getStrategyList(strategy, myPage);
    }

    /**
     * 获取策略列表_全部_简要
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("getStrategyListEx")
    public Result getStrategyListEx(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        Admin admin = (Admin) request.getAttribute("obj_admin");
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (role.getFromSoftId().equals("0")) {
            return Result.error("超级管理员无法使用此接口");
        }
        //strategy.setFromSoftId(Integer.parseInt(role.getFromSoftId()));
        if (CheckUtils.isObjectEmpty(strategy.getType())) {
            return Result.error("type参数不能为空");
        }
        return strategyService.getStrategyListEx(strategy);
    }

    /**
     * 查询策略，根据id
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("getStrategy")
    public Result getStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        return strategyService.getStrategy(strategy);
    }

    /**
     * 修改策略
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("updStrategy")
    public Result updStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        if (CheckUtils.isObjectEmpty(strategy.getName()) && CheckUtils.isObjectEmpty(strategy.getType())
                && CheckUtils.isObjectEmpty(strategy.getValue()) && CheckUtils.isObjectEmpty(strategy.getSort())
                && CheckUtils.isObjectEmpty(strategy.getPrice()) && CheckUtils.isObjectEmpty(strategy.getFromSoftId())
                && CheckUtils.isObjectEmpty(strategy.getStatus())) {
            return Result.error("参数不能全部为空");
        }
        return strategyService.updStrategy(strategy);
    }

    /**
     * 添加策略
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("addStrategy")
    public Result addStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getName()) || CheckUtils.isObjectEmpty(strategy.getType())
                || CheckUtils.isObjectEmpty(strategy.getValue()) || CheckUtils.isObjectEmpty(strategy.getSort())
                || CheckUtils.isObjectEmpty(strategy.getPrice()) || CheckUtils.isObjectEmpty(strategy.getFromSoftId())
                || CheckUtils.isObjectEmpty(strategy.getStatus())) {
            return Result.error("参数不全");
        }
        return strategyService.addStrategy(strategy);
    }

    /**
     * 删除策略
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_admin = true)
    @PostMapping("delStrategy")
    public Result delStrategy(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Strategy strategy = jsonObject.toJavaObject(Strategy.class);
        if (CheckUtils.isObjectEmpty(strategy)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(strategy.getId())) {
            return Result.error("id不能为空");
        }
        return strategyService.delStrategy(strategy);
    }
}
