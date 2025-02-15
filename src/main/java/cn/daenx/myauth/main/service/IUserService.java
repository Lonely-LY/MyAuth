package cn.daenx.myauth.main.service;

import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Soft;
import cn.daenx.myauth.main.entity.User;
import cn.daenx.myauth.main.entity.Version;
import cn.daenx.myauth.main.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author DaenMax
 * @since 2022-01-07
 */
public interface IUserService extends IService<User> {
    /**
     * 注册
     *
     * @param user
     * @param softC
     * @return
     */
    Result register(User user, Soft softC);

    /**
     * 登录
     *
     * @param user
     * @param softC
     * @return
     */
    Result login(User user, Soft softC);

    /**
     * 检查账号状态
     *
     * @param user
     * @param softC
     * @return
     */
    Result checkUser(User user, Soft softC);

    /**
     * 心跳
     *
     * @param user
     * @param softC
     * @return
     */
    Result heart(User user, Soft softC, String token);

    /**
     * 离线
     *
     * @param user
     * @param softC
     * @param token
     * @return
     */
    Result offLine(User user, Soft softC, String token);

    /**
     * 使用卡密
     *
     * @param user
     * @param soft
     * @return
     */
    Result useCkey(User user, Soft soft);

    /**
     * 获取回复
     *
     * @param soft
     * @param version
     * @param keyword
     * @return
     */
    Result getMsg(Soft soft, Version version, String keyword, String ver);

    /**
     * 解绑
     *
     * @param user
     * @param soft
     * @return
     */
    Result unbind(User user, Soft soft);

    /**
     * 修改密码
     *
     * @param user
     * @param nowPass
     * @param newPass
     * @return
     */
    Result editPass(String user, String nowPass, String newPass, Soft soft);

    /**
     * 修改资料：QQ和昵称
     *
     * @param user
     * @param soft
     * @return
     */
    Result editInfo(User user, Soft soft, String token);

    /**
     * 获取用户列表
     *
     * @param user
     * @param myPage
     * @return
     */
    Result getUserList(User user, MyPage myPage);

    /**
     * 查询用户
     *
     * @param user
     * @return
     */
    Result getUser(User user);

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    Result updUser(User user);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    Result addUser(User user, Admin admin);

    /**
     * 删除用户，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result delUser(String ids);


    /**
     * 获取我的授权
     *
     * @param user
     * @param myPage
     * @return
     */
    Result getMyUserList(User user, MyPage myPage, Admin admin);

    /**
     * 自助修改账号
     *
     * @param user
     * @param newUser
     * @param pass
     * @param softId
     * @param ckey
     * @return
     */
    Result selfChangeUser(String user, String newUser, String pass, Integer softId, String ckey);

    /**
     * 查询账号信息
     *
     * @param user
     * @param soft
     * @return
     */
    Result queryUserInfo(String user, Soft soft);

    /**
     * 查询管理员信息
     *
     * @param user
     * @param soft
     * @return
     */
    Result queryAdminInfo(String user, Soft soft);

    /**
     * 验证用户卡密密码
     *
     * @param user
     * @return
     */
    boolean queryUserPassCKey(String user , String pass , String ckey , Soft soft);

    /**
     * 查询用户在线信息
     *
     * @param user
     * @return
     */
    Result queryUserOnlineInfo(User user);

    /**
     * 按条件批量操作用户授权
     *
     * @param minPoint
     * @param maxPoint
     * @param minAuthTime
     * @param maxAuthTime
     * @param minRegTime
     * @param maxRegTime
     * @param updPoint
     * @param updAuthTime
     * @return
     */
    Result updateUserAuthInfo(Integer fromSoftId , Integer minPoint , Integer maxPoint , Integer minAuthTime , Integer maxAuthTime , Integer minRegTime , Integer maxRegTime , Integer updPoint , Integer updAuthTime);
}
