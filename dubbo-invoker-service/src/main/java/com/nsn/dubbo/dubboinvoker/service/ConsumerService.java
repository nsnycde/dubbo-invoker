package com.nsn.dubbo.dubboinvoker.service;

import com.nsn.dubbo.dubboinvoker.dal.po.Consumer;

import java.util.List;

/**
 * @author nsn
 */
public interface ConsumerService {

    /**
     * 查询所有
     * @return
     */
    List<Consumer> selectAllConsumer();

    /**
     * 查询所有 指定用户
     * @return
     */
    List<Consumer> selectAllConsumerByUserId(Long userId);

    /**
     * 添加消费者配置
     * @param url
     * @param group
     * @param version
     * @return
     */
    int addConsumer(String url, String group, String version);

    /**
     * 添加消费者配置
     * @param name
     * @return
     */
    int addConsumer(String name);

    /**
     * 查询
     * @param name
     * @return
     */
    Consumer selectConsumerByName(String name);

    /**
     * 删除消费者配置
     * @param consumerId
     * @return
     */
    int delConsumer(Long consumerId);

    /**
     * 删除消费者配置
     * @param consumerId
     * @return
     */
    Consumer selectConsumerById(Long consumerId);
}
