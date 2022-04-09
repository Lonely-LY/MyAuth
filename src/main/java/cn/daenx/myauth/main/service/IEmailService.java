package cn.daenx.myauth.main.service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 发送邮件
 * Created by Panyoujie on 2019-06-19 04:07
 */
public interface IEmailService {
    /**
     * 发送普通邮件
     *
     * @param title    标题
     * @param content  内容
     * @param toEmails 收件人
     */
    void sendTextEmail(String title, String content, String[] toEmails) throws UnsupportedEncodingException, AddressException;

    /**
     * 发送富文本邮件
     *
     * @param title    标题
     * @param html     富文本
     * @param toEmails 收件人
     */
    void sendFullTextEmail(String title, String html, String[] toEmails) throws MessagingException, UnsupportedEncodingException;

    /**
     * 发送html模板邮件
     *
     * @param title    标题
     * @param htmlPath html路径
     * @param map      填充数据
     * @param toEmails 收件人
     * @throws MessagingException MessagingException
     * @throws IOException        IOException
     */
    void sendHtmlEmail(String title, String htmlPath, Map<String, Object> map, String[] toEmails) throws MessagingException, IOException;

}