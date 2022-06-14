package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.EpayOrders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author
 * @since 2022-05-13
 */
public interface IEpayOrdersService extends IService<EpayOrders> {

    /**
     * 获取订单列表
     *
     * @param epayOrders
     * @param myPage
     * @return
     */
    Result getEpayOrdersList(EpayOrders epayOrders, MyPage myPage);

    /**
     * 查询epay订单
     *
     * @param outTradeNo
     * @return
     */
    Result getEpayOrder(String outTradeNo);

    /**
     * 删除订单，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    Result delEpayOrders(String ids);

}
