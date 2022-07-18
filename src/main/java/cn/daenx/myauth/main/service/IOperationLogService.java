package cn.daenx.myauth.main.service;

import cn.daenx.myauth.base.vo.MyPage;
import cn.daenx.myauth.base.vo.Result;
import cn.daenx.myauth.main.entity.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOperationLogService extends IService<OperationLog> {

    /**
     * 获取操作日志列表
     *
     * @param operationLog
     * @param myPage
     * @return
     */
    Result getOperationLogList(OperationLog operationLog, MyPage myPage);

}
