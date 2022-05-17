package cn.daenx.myauth.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author
 * @since 2022-05-16 19:59
 */
@Data
@Accessors(chain = true)
@TableName("ma_mail_send")
public class MailSend {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 发送开关(1开打,0关闭)
     */
    private Integer sendSwitch;

    /**
     * 主题信息
     */
    private String sendTheme;

    /**
     * 标题信息
     */
    private String sendTitle;

    /**
     * 模板文件
     */
    private String sendTemplates;

}
