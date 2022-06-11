package cn.daenx.myauth.main.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("ma_epay_config")
public class Epay extends Model {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 通道名称
     */
    private String name;

    /**
     * 易支付接口地址
     */
    private String url;

    /**
     * 商户ID
     */
    private Integer pid;

    /**
     * 商户密匙
     */
    private String ekey;

    /**
     * 异步通知地址(后端地址)
     */
    private String notifyUrl;

    /**
     * 支付成功跳转地址
     */
    private String returnUrl;

    /**
     * 微信支付开关(1开启0关闭)
     */
    private Integer wxpaySwitch;

    /**
     * 支付宝支付开关(1开启0关闭)
     */
    private Integer alipaySwitch;

    /**
     * QQ支付开关(1开启0关闭)
     */
    private Integer qqpaySwitch;

}
