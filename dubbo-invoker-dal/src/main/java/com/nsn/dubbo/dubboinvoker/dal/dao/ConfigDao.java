package com.nsn.dubbo.dubboinvoker.dal.dao;

import com.nsn.dubbo.dubboinvoker.dal.mapper.ConfigMapper;
import com.nsn.dubbo.dubboinvoker.dal.po.Config;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 自定义操作数据库接口
 *
 * @author nsn
 */
@Repository
public interface ConfigDao extends ConfigMapper {

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
