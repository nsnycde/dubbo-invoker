package com.nsn.dubbo.dubboinvoker.service.core;

import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;

import java.util.List;
import java.util.Map;

/**
 * 描述: 缓存接口
 *
 * @author nsn
 */
public interface CacheService {


    /**
     * 获取consumer缓存
     * @param consumerName
     * @return
     */
    ConsumerBO selectConsumerByName(String consumerName);

    /**
     * 存入consumer缓存
     * @param consumerId
     * @param name
     * @param init
     * @return
     */
    ConsumerBO putConsumer(Long consumerId, String name, boolean init);

    /**
     * 删除consumer缓存
     * @param name
     * @return
     */
    ConsumerBO removeConsumer(String name);

    /**
     * 获取所有的私服地址
     * @return
     */
    Map<Long,String> selectAllRepository();

    /**
     * 获取所有的依赖配置
     * @return
     */
    Map<Long,String> selectAllDependency();

    /**
     * 获取所有consumer
     * @return
     */
    Map<String,ConsumerBO> selectConsumerMap();

    /**
     * 获取jarPathMap
     * @return
     */
    Map<String,String> selectJarPathMap();

    /**
     * 重载所有consumer
     * @return
     */
    void reloadAllConsumer();

    /**
     * 重载指定consumer
     * @param name
     */
    void reloadConsumer(String name);

    /**
     * jarPath加入缓存
     * @param jarPath
     */
    void putJarPath(String jarPath);

    /**
     * 重载所有配置
     * @return
     */
    void reloadAllConfig();

    /**
     * 删除config缓存
     * @param configId
     * @param configKey
     */
    void removeConfig(Long configId, String configKey);

    /**
     * 配置存入缓存
     * @param configId
     * @param configKey
     * @param configValue
     * @return
     */
    void putConfig(Long configId, String configKey, String configValue);

    /**
     * 校验config是否存在
     * @param configKey
     * @param configValue
     * @return
     */
    boolean checkConfigExist(String configKey, String configValue);

    /**
     * 校验config是否正确
     * @param configKey
     * @param configValue
     * @return
     */
    boolean checkConfigCorrect(String configKey, String configValue);

    /**
     * 热部署
     * @param jarNameList
     */
    void hotDeploy(List<String> jarNameList);
}
