package cn.daenx.myauth.main.service.impl;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.OperationLog;
import cn.daenx.myauth.main.mapper.OperationLogMapper;
import cn.daenx.myauth.main.service.IOperationLogService;
import cn.daenx.myauth.util.CheckUtils;
import cn.daenx.myauth.util.MyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {
    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 获取操作日志列表
     *
     * @param operationLog
     * @param myPage
     * @return
     */
    @Override
    public Result getOperationLogList(OperationLog operationLog, MyPage myPage) {
        Page<OperationLog> page = new Page<>(myPage.getPageIndex(), myPage.getPageSize(), true);
        if (!CheckUtils.isObjectEmpty(myPage.getOrders())) {
            for (int i = 0; i < myPage.getOrders().size(); i++) {
                myPage.getOrders().get(i).setColumn(MyUtils.camelToUnderline(myPage.getOrders().get(i).getColumn()));
            }
            page.setOrders(myPage.getOrders());
        }
        IPage<OperationLog> msgPage = operationLogMapper.selectPage(page,getQwOperationLog(operationLog));
        return Result.ok("获取成功", msgPage);
    }

    /**
     * 获取查询条件构造器
     *
     * @param operationLog
     * @return
     */
    public LambdaQueryWrapper<OperationLog> getQwOperationLog(OperationLog operationLog) {
        LambdaQueryWrapper<OperationLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(!CheckUtils.isObjectEmpty(operationLog.getId()),OperationLog::getId,operationLog.getId());
        lambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(operationLog.getOperationIp()),OperationLog::getOperationIp,operationLog.getOperationIp());
        lambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(operationLog.getOperationUa()),OperationLog::getOperationUa,operationLog.getOperationUa());
        lambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(operationLog.getOperationTime()),OperationLog::getOperationTime,operationLog.getOperationTime());
        lambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(operationLog.getOperationType()),OperationLog::getOperationType,operationLog.getOperationType());
        lambdaQueryWrapper.like(!CheckUtils.isObjectEmpty(operationLog.getOperationUser()),OperationLog::getOperationUser,operationLog.getOperationUser());
        return lambdaQueryWrapper;
    }
}
