package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.MailSend;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author
 * @since 2022-05-16 20:07
 */
public interface IMailSendService extends IService<MailSend> {

    /**
     * 按ID或者发送类型获取发送配置
     *
     * @param mailSend
     * @return
     */
    Result getMailSend(MailSend mailSend);

    /**
     * 修改发送配置
     *
     * @param mailSend
     * @return
     */
    Result updMailSend(MailSend mailSend);

    /**
     * 获取全部发送类型
     *
     * @return
     */
    Result getSendTypeList();

    /**
     * 获取所有发送配置
     *
     * @return
     */
    Result getMailSendList(MailSend mailSend, MyPage myPage);

    /**
     * 邮件发送测试
     *
     * @param sendMail
     * @param theme
     * @param txt
     * @return
     */
    Result sendMailTest(String sendMail, String theme, String txt);

    /**
     * 开启发送，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result openSendMail(String ids);

    /**
     * 关闭发送，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result turnOffSend(String ids);
}
