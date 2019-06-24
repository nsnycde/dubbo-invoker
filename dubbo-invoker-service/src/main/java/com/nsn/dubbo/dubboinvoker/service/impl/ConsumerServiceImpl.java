package com.nsn.dubbo.dubboinvoker.service.impl;

import com.nsn.dubbo.dubboinvoker.dal.dao.ConsumerDao;
import com.nsn.dubbo.dubboinvoker.dal.po.Consumer;
import com.nsn.dubbo.dubboinvoker.dal.po.ConsumerExample;
import com.nsn.dubbo.dubboinvoker.service.ConsumerService;
import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;
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
public class ConsumerServiceImpl implements ConsumerService {

    @Resource
    private ConsumerDao consumerDao;

    @Override
    public List<Consumer> selectAllConsumer() {
        return consumerDao.selectByExample(new ConsumerExample());
    }

    @Override
    public List<Consumer> selectAllConsumerByUserId(Long userId) {
        Assert.notNull(userId, "userId不能为空!");
        ConsumerExample example = new ConsumerExample();
        example.createCriteria().andUserIdEqualTo(userId);
        return consumerDao.selectByExample(example);
    }

    @Override
    public int addConsumer(String url, String group, String version) {
        Consumer consumer = new Consumer();
        consumer.setName(ConsumerBO.buildName(url, group, version));
        consumer.setUserId(0L);
        return this.insertSelective(consumer);
    }

    @Override
    public int addConsumer(String name) {
        Assert.hasText(name, "name不能为空!");
        Consumer consumer = new Consumer();
        consumer.setName(name);
        consumer.setUserId(0L);
        return this.insertSelective(consumer);
    }

    @Override
    public int delConsumer(Long consumerId) {
        Assert.notNull(consumerId, "consumerId不能为空!");
        return consumerDao.deleteByPrimaryKey(consumerId);
    }

    @Override
    public Consumer selectConsumerByName(String name) {
        Assert.hasText(name, "name不能为空!");
        ConsumerExample example = new ConsumerExample();
        example.createCriteria().andNameEqualTo(name);
        List<Consumer> consumers = consumerDao.selectByExample(example);
        if(consumers.size() == 0){
            throw new BizException(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
        }else if(consumers.size() > 1){
            throw new BizException(ReturnEnum.EXCEPTION_CONSUMER_NOT_UNIQUE);
        }
        return consumers.get(0);
    }

    @Override
    public Consumer selectConsumerById(Long consumerId) {
        Assert.notNull(consumerId, "consumerId不能为空!");
        return consumerDao.selectByPrimaryKey(consumerId);
    }

    /**
     * 包装dao
     * @param consumer
     * @return
     */
    private int insertSelective(Consumer consumer){
        consumer.setCreateTime(new Date());
        return consumerDao.insertSelective(consumer);
    }
}
