package com.nsn.dubbo.dubboinvoker.service.core;

import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;

import java.util.List;

/**
 * 描述: 调用接口
 *
 * @author donghao
 */
public interface InvokeService {

    /**
     * 远程调用
     * @param userId  用户id
     * @param consumerBO 消费者
     * @param methodName 方法
     * @param params 请求参数
     * @return
     */
    Object invoke(Long userId, ConsumerBO consumerBO, String methodName, List<List<String>> params);
}
