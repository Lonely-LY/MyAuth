package cn.daenx.myauth.main.controller.web;

import cn.daenx.myauth.base.vo.CardExportVo;
import cn.daenx.myauth.main.entity.Role;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.ExcelUtil;
import cn.daenx.myauth.util.ExportXls;
import cn.daenx.myauth.base.annotation.AdminLogin;
import cn.daenx.myauth.base.annotation.NoEncryptNoSign;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Card;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.main.service.IAdminService;
import cn.daenx.myauth.main.service.ICardService;
import cn.daenx.myauth.util.RedisUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 前端web使用的API接口
 *
 * @author DaenMax
 */
@Slf4j
@RestController
@RequestMapping("/web")
public class CardController {
    @Resource
    private ICardService cardService;
    @Resource
    private IAdminService adminService;
    @Resource
    private RedisUtil redisUtil;


    /**
     * 导出卡密
     *
     * @param token
     * @param card
     * @param request
     * @param response
     * @throws IOException
     */
    @NoEncryptNoSign
    @GetMapping("exportCard")
    public void exportCard(String token, Card card, HttpServletRequest request, HttpServletResponse response) {
        Admin admin = adminService.tokenIsOk(token);
        if (CheckUtils.isObjectEmpty(admin)) {
            return;
        }
        if (CheckUtils.isObjectEmpty(card)) {
            return;
        }
        Role role = (Role) redisUtil.get("role:" + admin.getRole());
        if (!role.getFromSoftId().equals("0")) {
            card.setFromAdminId(admin.getId());
        }
        List<CardExportVo> cardExportVos = cardService.exportCardVO(card);
        ExcelUtil.exportXlsx(request,response, "卡密导出", "卡密列表", cardExportVos, CardExportVo.class);
        return;
    }

    /**
     * 获取卡密列表
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getCardList")
    public Result getCardList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        if (CheckUtils.isObjectEmpty(card) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(card.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        return cardService.getCardList(card, myPage);
    }

    /**
     * 查询卡密，根据id或者ckey
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("getCard")
    public Result getCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(card.getId()) && CheckUtils.isObjectEmpty(card.getCkey())) {
            return Result.error("id和ckey不能都为空");
        }
        return cardService.getCard(card);
    }

    /**
     * 修改卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("updCard")
    public Result updCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(card.getId())) {
            return Result.error("id不能为空");
        }
        card.setStatus(null);
        card.setLetTime(null);
        card.setLetUser(null);
        card.setFromSoftId(null);
        card.setAddTime(null);
        if (CheckUtils.isObjectEmpty(card.getCkey()) && CheckUtils.isObjectEmpty(card.getPoint())
                && CheckUtils.isObjectEmpty(card.getSeconds())) {
            return Result.error("参数不能全部为空");
        }
        return cardService.updCard(card);
    }

    /**
     * 生成卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("addCard")
    public Result addCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        String prefix = jsonObject.getString("prefix");
        Integer count = jsonObject.getInteger("count");
        if (CheckUtils.isObjectEmpty(card)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(count)) {
            return Result.error("count参数不能为空");
        }
        if (count == 0) {
            return Result.error("生成张数不能为0");
        }
        if (CheckUtils.isObjectEmpty(card.getFromSoftId())) {
            return Result.error("fromSoftId参数不能为空");
        }
        if (CheckUtils.isObjectEmpty(card.getSeconds()) && CheckUtils.isObjectEmpty(card.getPoint())) {
            return Result.error("seconds和point不能都为空");
        }
        if (CheckUtils.isObjectEmpty(card.getSeconds())) {
            card.setSeconds(0);
        }
        if (CheckUtils.isObjectEmpty(card.getPoint())) {
            card.setPoint(0);
        }
        Admin myAdmin = (Admin) request.getAttribute("obj_admin");
        return cardService.addCard(prefix, count, card, myAdmin);
    }

    /**
     * 删除卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("delCard")
    public Result delCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.delCard(ids);
    }

    /**
     * 禁用卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("banCard")
    public Result banCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.banCard(ids);
    }

    /**
     * 解禁卡密（支持批量）
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin
    @PostMapping("unBanCard")
    public Result unBanCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String ids = jsonObject.getString("ids");
        if (CheckUtils.isObjectEmpty(ids)) {
            return Result.error("ids不能为空，多个用英文逗号隔开");
        }
        return cardService.unBanCard(ids);
    }

    /**
     * 获取我的卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("getMyCardList")
    public Result getMyCardList(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        Card card = jsonObject.toJavaObject(Card.class);
        MyPage myPage = jsonObject.toJavaObject(MyPage.class);
        Admin admin = (Admin) request.getAttribute("obj_admin");
        if (CheckUtils.isObjectEmpty(card) || CheckUtils.isObjectEmpty(myPage)) {
            return Result.error("参数错误");
        }
        if (CheckUtils.isObjectEmpty(myPage.getPageIndex()) || CheckUtils.isObjectEmpty(myPage.getPageSize())) {
            return Result.error("页码和尺寸参数不能为空");
        }
        return cardService.getMyCardList(card, myPage, admin);
    }

    /**
     * 生成卡密
     *
     * @param request
     * @return
     */
    @NoEncryptNoSign
    @AdminLogin(is_super_role = false)
    @PostMapping("addMyCard")
    public Result addMyCard(HttpServletRequest request) {
        JSONObject jsonObject = (JSONObject) request.getAttribute("json");
        String prefix = jsonObject.getString("prefix");
        Integer count = jsonObject.getInteger("count");
        Integer strategyId = jsonObject.getInteger("strategyId");
        if (CheckUtils.isObjectEmpty(count)) {
            return Result.error("count参数不能为空");
        }
        if (count == 0) {
            return Result.error("生成张数不能为0");
        }
        if (strategyId == 0) {
            return Result.error("策略ID错误");
        }
        Admin myAdmin = (Admin) request.getAttribute("obj_admin");
        return cardService.addMyCard(strategyId, prefix, count, myAdmin);
    }

}
