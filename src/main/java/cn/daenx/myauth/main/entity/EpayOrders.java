package cn.daenx.myauth.main.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("ma_epay_orders")
public class EpayOrders extends Model {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 易支付订单号
     */
    private String tradeNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 支付类型(alipay:支付宝,tenpay:财付通,qqpay:QQ钱包,wxpay:微信支付)
     */
    private String type;

    /**
     * 创建订单时间
     */
    private String addtime;

    /**
     * 完成交易时间
     */
    private String endtime;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品金额
     */
    private String money;

    /**
     * 支付状态(1为支付成功,0为未支付)
     */
    private Integer status;

    /**
     * 创建订单管理员
     */
    private Integer fromAdminId;

}
