package com.nsn.dubbo.dubboinvoker.service;

/**
 * @author nsn
 */
public interface InvokeLogService {

    /**
     * 保存调用日志
     * @param method
     * @param req
     * @param rsp
     * @param consumerId
     * @param userId
     * @return
     */
    int addLog(String method, Object req, Object rsp, Long consumerId, Long userId);

    /**
     * 查询最后一次调用请求
     * @param method
     * @param consumerId
     * @param userId
     * @return
     */
    String selectLastReqJson(String method, Long consumerId, Long userId);
}
