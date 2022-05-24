package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.Admin;
import cn.daenx.myauth.main.entity.Epay;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * @author
 * @since 2022-05-13
 */
public interface IEpayService extends IService<Epay> {

    /**
     * 获取epay配置
     *
     * @return
     */
    Result getEpay();


    /**
     * 修改epay配置
     *
     * @param epay
     * @return
     */
    Result editEpay(Epay epay);

    /**
     * 充值金额
     *
     * @param money
     * @param admin
     * @return
     */
    Result depositMoneyLink(BigDecimal money , String type , Admin admin);

    /**
     * 处理epay异步通知
     *
     * @param pid
     * @param trade_no
     * @param out_trade_no
     * @param type
     * @param name
     * @param money
     * @param trade_status
     * @param sign
     * @param sign_type
     */
    Result epayNotify(Integer pid ,
                    String trade_no ,
                    String out_trade_no ,
                    String type ,
                    String name ,
                    String money ,
                    String trade_status ,
                    String sign ,
                    String sign_type);
}