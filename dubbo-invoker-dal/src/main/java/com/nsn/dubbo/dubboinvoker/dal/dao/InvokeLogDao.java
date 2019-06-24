package com.nsn.dubbo.dubboinvoker.dal.dao;

import com.nsn.dubbo.dubboinvoker.dal.mapper.InvokeLogMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 自定义操作数据库接口
 *
 * @author nsn
 */
@Repository
public interface InvokeLogDao extends InvokeLogMapper {

    /**
     * 查询最后一次调用请求
     * @param method
     * @param consumerId
     * @param userId
     * @return
     */
    String selectLastReqJson(@Param("method") String method, @Param("consumerId") Long consumerId, @Param("userId") Long userId);
}
