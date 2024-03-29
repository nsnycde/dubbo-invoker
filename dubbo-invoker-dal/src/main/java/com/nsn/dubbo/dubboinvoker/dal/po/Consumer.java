package com.nsn.dubbo.dubboinvoker.dal.po;

import java.util.Date;

public class Consumer {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CONSUMER.CONSUMER_ID
     *
     * @mbggenerated
     */
    private Long consumerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CONSUMER.USER_ID
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CONSUMER.NAME
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CONSUMER.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column CONSUMER.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CONSUMER.CONSUMER_ID
     *
     * @return the value of CONSUMER.CONSUMER_ID
     *
     * @mbggenerated
     */
    public Long getConsumerId() {
        return consumerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CONSUMER.CONSUMER_ID
     *
     * @param consumerId the value for CONSUMER.CONSUMER_ID
     *
     * @mbggenerated
     */
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CONSUMER.USER_ID
     *
     * @return the value of CONSUMER.USER_ID
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CONSUMER.USER_ID
     *
     * @param userId the value for CONSUMER.USER_ID
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CONSUMER.NAME
     *
     * @return the value of CONSUMER.NAME
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CONSUMER.NAME
     *
     * @param name the value for CONSUMER.NAME
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CONSUMER.CREATE_TIME
     *
     * @return the value of CONSUMER.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CONSUMER.CREATE_TIME
     *
     * @param createTime the value for CONSUMER.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column CONSUMER.UPDATE_TIME
     *
     * @return the value of CONSUMER.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column CONSUMER.UPDATE_TIME
     *
     * @param updateTime the value for CONSUMER.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}