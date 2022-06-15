package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.EpayOrders;
import cn.daenx.myauth.main.mapper.EpayMapper;
import cn.daenx.myauth.main.mapper.EpayOrdersMapper;
import cn.daenx.myauth.main.service.IEpayOrdersService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author
 * @since 2022-05-13
 */
@Service
public class EpayOrdersServiceImpl extends ServiceImpl<EpayOrdersMapper, EpayOrders> implements IEpayOrdersService {

    @Resource
    private EpayOrdersMapper epayOrdersMapper;
    @Resource
    private EpayMapper eapyMapper;

    /**
     * 获取查询条件构造器
     *
     * @param epayOrders
     * @return
     */
    public LambdaQueryWrapper<EpayOrders> getQwEpayOrders(EpayOrders epayOrders) {
        LambdaQueryWrapper<EpayOrders> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getTradeNo()), EpayOrders::getTradeNo, epayOrders.getTradeNo());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getOutTradeNo()), EpayOrders::getOutTradeNo, epayOrders.getOutTradeNo());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getType()), EpayOrders::getType, epayOrders.getType());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getAddtime()), EpayOrders::getAddtime, epayOrders.getAddtime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getEndtime()), EpayOrders::getEndtime, epayOrders.getEndtime());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getName()), EpayOrders::getName, epayOrders.getName());
        LambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(epayOrders.getMoney()), EpayOrders::getMoney, epayOrders.getMoney());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epayOrders.getStatus()), EpayOrders::getStatus, epayOrders.getStatus());
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(epayOrders.getFromAdminId()), EpayOrders::getFromAdminId, epayOrders.getFromAdminId());
        LambdaQueryWrapper.orderBy(true, false, EpayOrders::getId);
        return LambdaQueryWrapper;
    }

    /**
     * 获取epay订单列表
     *
     * @param epayOrders
     * @param myPage
     * @return
     */
    @Override
    public Result getEpayOrdersList(EpayOrders epayOrders, MyPage myPage) {
        Page<EpayOrders> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<EpayOrders> epayOrdersPage = epayOrdersMapper.selectPage(page, getQwEpayOrders(epayOrders));
        return Result.ok("获取成功", epayOrdersPage);
    }

    /**
     * 查询订单
     *
     * @param outTradeNo
     * @return
     */
    @Override
    public Result getEpayOrder(String outTradeNo) {
        LambdaQueryWrapper<EpayOrders> LambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(outTradeNo), EpayOrders::getOutTradeNo, outTradeNo);
        EpayOrders epayOrders = epayOrdersMapper.selectOne(LambdaQueryWrapper);
        if(!CheckUtils.isObjectEmpty(epayOrders)){
            return Result.ok("查询成功",epayOrders);
        }
        return Result.error("查询失败");
    }

    /**
     * 删除epay订单，支持批量
     *
     * @param ids 多个用英文逗号隔开
     * @return
     */
    @Override
    public Result delEpayOrders(String ids) {
        String[] idArray = ids.split(",");
        List<String> strings = Arrays.asList(idArray);
        if (idArray.length == 0) {
            return Result.error("ids参数格式可能错误");
        }
        int okCount = epayOrdersMapper.deleteBatchIds(strings);
        return Result.ok("成功删除 " + okCount + " 笔订单记录");
    }

}
