package com.nsn.dubbo.dubboinvoker.service;

import com.nsn.dubbo.dubboinvoker.dal.po.Config;

import java.util.List;

/**
 * @author nsn
 */
public interface ConfigService {

    /**
     * 配置类型  maven 依赖
     */
    String CONFIG_TYPE_DEPENDENCY = "maven-dependency";
    /**
     * 配置类型  nexus 私服 仓库
     */
    String CONFIG_TYPE_REPOSITORY = "nexus-repository";

    /**
     * 添加私服仓库
     * @param repository
     * @return
     */
    int addRepository(String repository);

    /**
     * 添加maven依赖
     * @param dependency
     * @return
     */
    int addDependency(String dependency);

    /**
     * 添加config
     * @param configKey
     * @param configValue
     * @return
     */
    int addConfig(String configKey, String configValue);

    /**
     * 查询
     * @param configKey
     * @param configValue
     * @return
     */
    Config selectConfig(String configKey, String configValue);

    /**
     * 删除配置
     * @param configId
     * @return
     */
    int delConfig(Long configId);

    /**
     * 获取所有的私服地址
     * @return
     */
    List<Config> selectAllRepository();

    /**
     * 获取所有的jar依赖地址
     * @return
     */
    List<Config> selectAllDependency();
}
