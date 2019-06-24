package com.nsn.dubbo.dubboinvoker.service.impl;

import com.alibaba.fastjson.JSON;
import com.nsn.dubbo.dubboinvoker.dal.dao.InvokeLogDao;
import com.nsn.dubbo.dubboinvoker.dal.po.InvokeLog;
import com.nsn.dubbo.dubboinvoker.service.InvokeLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author nsn
 */
@Slf4j
@Service
public class InvokeLogServiceImpl implements InvokeLogService {

    @Resource
    private InvokeLogDao invokeLogDao;

    @Override
    public int addLog(String method, Object req, Object rsp, Long consumerId, Long userId) {
        Assert.hasText(method, "method不能为空!");
        Assert.notNull(consumerId, "consumerId不能为空!");
        Assert.notNull(userId, "userId不能为空!");
        InvokeLog invokeLog = new InvokeLog();
        invokeLog.setConsumerId(consumerId);
        invokeLog.setUserId(userId);
        invokeLog.setMethod(method);
        if(req != null){
            invokeLog.setReq(JSON.toJSONString(req));
        }
        if(rsp != null){
            invokeLog.setRsp(JSON.toJSONString(rsp));
        }
        invokeLog.setCreateTime(new Date());
        return invokeLogDao.insertSelective(invokeLog);
    }

    @Override
    public String selectLastReqJson(String method, Long consumerId, Long userId) {
        Assert.hasText(method, "method不能为空!");
        Assert.notNull(consumerId, "consumerId不能为空!");
        Assert.notNull(userId, "userId不能为空!");
        return invokeLogDao.selectLastReqJson(method, consumerId, userId);
    }
}
