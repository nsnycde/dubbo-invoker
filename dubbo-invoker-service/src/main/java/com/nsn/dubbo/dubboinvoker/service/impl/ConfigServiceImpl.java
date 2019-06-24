package com.nsn.dubbo.dubboinvoker.service.impl;

import com.nsn.dubbo.dubboinvoker.dal.dao.ConfigDao;
import com.nsn.dubbo.dubboinvoker.dal.po.Config;
import com.nsn.dubbo.dubboinvoker.dal.po.ConfigExample;
import com.nsn.dubbo.dubboinvoker.service.ConfigService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author nsn
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigDao configDao;

    @Override
    public int addRepository(String repository) {
        Assert.hasText(repository, "repository不能为空!");
        Config config = new Config();
        config.setKey(CONFIG_TYPE_REPOSITORY);
        config.setValue(repository);
        return this.insertSelective(config);
    }

    @Override
    public int addDependency(String dependency) {
        Assert.hasText(dependency, "dependency不能为空!");
        Config config = new Config();
        config.setKey(CONFIG_TYPE_DEPENDENCY);
        config.setValue(dependency);
        return this.insertSelective(config);
    }

    @Override
    public int addConfig(String configKey, String configValue) {
        Assert.hasText(configKey, "configKey不能为空!");
        if(CONFIG_TYPE_DEPENDENCY.equals(configKey)){
            return this.addDependency(configValue);
        }else if(CONFIG_TYPE_REPOSITORY.equals(configKey)){
            return this.addRepository(configValue);
        }
        return 0;
    }

    @Override
    public Config selectConfig(String configKey, String configValue) {
        Assert.hasText(configKey, "configKey不能为空!");
        Assert.hasText(configValue, "configValue不能为空!");
        ConfigExample example = new ConfigExample();
        example.createCriteria().andKeyEqualTo(configKey).andValueEqualTo(configValue);
        List<Config> configList = configDao.selectByExample(example);
        if(configList.size() == 0){
            throw new BizException(ReturnEnum.EXCEPTION_CONFIG_NOT_EXIST);
        }else if(configList.size() > 1){
            throw new BizException(ReturnEnum.EXCEPTION_CONFIG_NOT_UNIQUE);
        }
        return configList.get(0);
    }

    @Override
    public int delConfig(Long configId) {
        Assert.notNull(configId, "configId不能为空!");
        return configDao.deleteByPrimaryKey(configId);
    }

    @Override
    public List<Config> selectAllRepository() {
        return configDao.selectAllRepository();
    }

    @Override
    public List<Config> selectAllDependency() {
        return configDao.selectAllDependency();
    }

    /**
     * 包装dao
     * @param record
     * @return
     */
    private int insertSelective(Config record){
        record.setCreateTime(new Date());
        return configDao.insertSelective(record);
    }
}
