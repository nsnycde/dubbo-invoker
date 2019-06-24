package com.nsn.dubbo.dubboinvoker.service.core.impl;

import com.alibaba.dubbo.common.bytecode.ClassGenerator;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.config.model.ApplicationModel;
import com.nsn.dubbo.dubboinvoker.dal.po.Consumer;
import com.nsn.dubbo.dubboinvoker.kit.ClassLoaderKit;
import com.nsn.dubbo.dubboinvoker.kit.MavenKit;
import com.nsn.dubbo.dubboinvoker.service.ConfigService;
import com.nsn.dubbo.dubboinvoker.service.ConsumerService;
import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;
import com.nsn.dubbo.dubboinvoker.service.core.CacheService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 描述: 默认的缓存实现
 *
 * @author nsn
 */
@Slf4j
@Component
public class DefaultCacheServiceImpl implements CacheService {

    /**
     * 消费者缓存
     */
    private final ConcurrentHashMap<String, ConsumerBO> consumerMap = new ConcurrentHashMap<>();

    /**
     * jarPathMap
     */
    private final ConcurrentHashMap<String, String> jarPathMap = new ConcurrentHashMap<>();

    /**
     * 私服集合
     */
    private final Map<Long, String> nexusMap = new ConcurrentHashMap<>();

    /**
     * 依赖集合
     */
    private final Map<Long, String> dependencyMap = new ConcurrentHashMap<>();

    @Resource
    private ConfigService configService;
    @Resource
    private ConsumerService consumerService;

    @PostConstruct
    public void doInit() {
        configService.selectAllRepository().forEach(config -> nexusMap.put(config.getId(), config.getValue()));
        configService.selectAllDependency().forEach(config -> dependencyMap.put(config.getId(), config.getValue()));

        //maven解析
        Set<String> jarPathSet = MavenKit.analyse(this.selectAllRepository().values()
                , this.selectAllDependency().values(), null, false);
        if(!CollectionUtils.isEmpty(jarPathSet)){
            jarPathSet.forEach(jarPath -> {
                ClassLoaderKit.addJar(jarPath);
                this.putJarPath(jarPath);
            });
        }
        //初始化消费者
        this.initAllConsumer();
    }

    /**
     * 初始化消费者
     */
    private void initAllConsumer(){
        List<Consumer> consumerList = consumerService.selectAllConsumerByUserId(0L);
        consumerList.forEach(consumer -> {
            try {
                this.putConsumer(consumer.getConsumerId(), consumer.getName(), false);
            } catch (Exception e) {
                log.error("出现异常消费者配置,consumerName:{}", consumer.getName());
            }
        });
    }

    /**
     * 销毁消费者
     */
    private void destroyAllConsumer(){
        consumerMap.values().forEach(consumerBO -> {
            if(consumerBO != null && consumerBO.getReferenceConfig() != null){
                consumerBO.getReferenceConfig().destroy();
            }
        });
        consumerMap.clear();
    }

    @Override
    public ConsumerBO selectConsumerByName(String consumerName){
        Assert.hasText(consumerName, "consumerName不能为空!");
        return consumerMap.get(consumerName);
    }

    @Override
    public ConsumerBO putConsumer(Long consumerId, String name, boolean init){
        Assert.notNull(consumerId, "consumerId不能为空!");
        Assert.hasText(name, "name不能为空!");
        ConsumerBO consumerBO = ConsumerBO.parseName(name, init);
        consumerBO.setConsumerId(consumerId);
        consumerMap.put(consumerBO.getName(), consumerBO);
        return consumerBO;
    }

    @Override
    public ConsumerBO removeConsumer(String name) {
        ConsumerBO consumerBO = consumerMap.remove(name);
        if(consumerBO != null && consumerBO.getReferenceConfig() != null){
            consumerBO.getReferenceConfig().destroy();
        }
        return consumerBO;
    }

    @Override
    public Map<Long,String> selectAllRepository() {
        return new HashMap<>(nexusMap);
    }

    @Override
    public Map<Long, String> selectAllDependency() {
        return new HashMap<>(dependencyMap);
    }

    @Override
    public Map<String,ConsumerBO> selectConsumerMap() {
        return new HashMap<>(consumerMap);
    }

    @Override
    public Map<String, String> selectJarPathMap() {
        return new HashMap<>(jarPathMap);
    }

    @Override
    public void reloadAllConsumer() {
        this.destroyAllConsumer();
        this.initAllConsumer();
    }

    @Override
    public void reloadConsumer(String name) {
        ConsumerBO consumerBO = this.removeConsumer(name);
        if(consumerBO != null){
            this.putConsumer(consumerBO.getConsumerId(), name, false);
        }
    }

    @Override
    public void putJarPath(String jarPath) {
        Assert.hasText(jarPath, "jarPath不能为空!");
        String key = jarPath.substring(jarPath.lastIndexOf(File.separator)+1);
        jarPathMap.put(key, jarPath);
    }

    @Override
    public void reloadAllConfig() {
        dependencyMap.clear();
        nexusMap.clear();
        configService.selectAllRepository().forEach(config -> nexusMap.put(config.getId(), config.getValue()));
        configService.selectAllDependency().forEach(config -> dependencyMap.put(config.getId(), config.getValue()));
    }

    @Override
    public void removeConfig(Long configId, String configKey) {
        Assert.notNull(configId, "configId不能为空!");
        Assert.hasText(configKey, "configKey不能为空!");
        if(ConfigService.CONFIG_TYPE_DEPENDENCY.equals(configKey)){
            dependencyMap.remove(configId);
        }else if(ConfigService.CONFIG_TYPE_REPOSITORY.equals(configKey)){
            nexusMap.remove(configId);
        }
    }

    @Override
    public void putConfig(Long configId, String configKey, String configValue) {
        Assert.notNull(configId, "configId不能为空!");
        Assert.hasText(configKey, "configKey不能为空!");
        Assert.hasText(configValue, "configValue不能为空!");
        if(ConfigService.CONFIG_TYPE_DEPENDENCY.equals(configKey)){
            dependencyMap.put(configId, configValue);
        }else if(ConfigService.CONFIG_TYPE_REPOSITORY.equals(configKey)){
            nexusMap.put(configId, configValue);
        }
    }

    @Override
    public boolean checkConfigExist(String configKey, String configValue) {
        if(ConfigService.CONFIG_TYPE_DEPENDENCY.equals(configKey)){
            return dependencyMap.values().contains(configValue);
        }else if(ConfigService.CONFIG_TYPE_REPOSITORY.equals(configKey)){
            return nexusMap.values().contains(configValue);
        }
        return false;
    }

    @Override
    public boolean checkConfigCorrect(String configKey, String configValue) {
        Assert.hasText(configKey, "configKey不能为空!");
        Assert.hasText(configValue, "configValue不能为空!");
        if(ConfigService.CONFIG_TYPE_REPOSITORY.equals(configKey)){
            return true;
        }
        if(ConfigService.CONFIG_TYPE_DEPENDENCY.equals(configKey)){
            //解析依赖
            List<String> dependencyList = new ArrayList<>(1);
            dependencyList.add(configValue);
            Set<String> jarPathSet = MavenKit.analyse(nexusMap.values(), dependencyList, null, false);
            if(!CollectionUtils.isEmpty(jarPathSet)){
                jarPathSet.removeAll(jarPathMap.values());
                jarPathSet.forEach(jarPath -> {
                    ClassLoaderKit.addJar(jarPath);
                    this.putJarPath(jarPath);
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public void hotDeploy(List<String> jarNameList) {
        if(CollectionUtils.isEmpty(jarNameList)){
            return;
        }
        List<String> deletePathList = new ArrayList<>(jarNameList.size());
        this.jarPathMap.forEach((key,value) -> {
            if(jarNameList.contains(key)){
                deletePathList.add(value);
            }
        });
        ClassLoaderKit.MyLoader loader = ClassLoaderKit.clearLoader();
        try {
            loader.close();
            //清除内置缓存
            this.destroyAllConsumer();
            //清除dubbo缓存
            Field filed = ApplicationModel.class.getDeclaredField("consumedServices");
            filed.setAccessible(true);
            ConcurrentMap map = (ConcurrentMap)filed.get(null);
            map.clear();
            filed = ReflectUtils.class.getDeclaredField("NAME_CLASS_CACHE");
            filed.setAccessible(true);
            map = (ConcurrentMap)filed.get(null);
            map.clear();
            filed = ClassGenerator.class.getDeclaredField("POOL_MAP");
            filed.setAccessible(true);
            map = (ConcurrentMap)filed.get(null);
            map.clear();
            //maven依赖解析
            Set<String> jarPathSet = MavenKit.analyse(this.nexusMap.values(), this.dependencyMap.values(), deletePathList, false);
            if(!CollectionUtils.isEmpty(jarPathSet)){
                jarPathSet.forEach(jarPath -> {
                    ClassLoaderKit.addJar(jarPath);
                    this.putJarPath(jarPath);
                });
            }
            //替换loader
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            int estimatedSize = group.activeCount() * 2;
            Thread[] slackList = new Thread[estimatedSize];
            // 获取根线程组的所有线程
            int actualSize = group.enumerate(slackList);
            Thread[] list = new Thread[actualSize];
            System.arraycopy(slackList, 0, list, 0, actualSize);
            for (Thread thread : list) {
                if(thread.getContextClassLoader() instanceof ClassLoaderKit.MyLoader){
                    thread.setContextClassLoader(ClassLoaderKit.getClassLoader());
                }
            }
            //消费者缓存
            this.initAllConsumer();
        } catch (Exception e) {
            throw new BizException(ReturnEnum.HOT_DEPLOY_FAIL);
        }

    }
}
