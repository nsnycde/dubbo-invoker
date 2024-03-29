package com.nsn.dubbo.dubboinvoker.dal.mapper;

import com.nsn.dubbo.dubboinvoker.dal.po.Consumer;
import com.nsn.dubbo.dubboinvoker.dal.po.ConsumerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConsumerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int countByExample(ConsumerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int deleteByExample(ConsumerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long consumerId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int insert(Consumer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int insertSelective(Consumer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    List<Consumer> selectByExample(ConsumerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    Consumer selectByPrimaryKey(Long consumerId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Consumer record, @Param("example") ConsumerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Consumer record, @Param("example") ConsumerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Consumer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table CONSUMER
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Consumer record);
}