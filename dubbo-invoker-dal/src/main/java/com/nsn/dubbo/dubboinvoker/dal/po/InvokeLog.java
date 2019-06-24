package com.nsn.dubbo.dubboinvoker.dal.po;

import java.util.Date;

public class InvokeLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.ID
     *
     * @mbggenerated
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.CONSUMER_ID
     *
     * @mbggenerated
     */
    private Long consumerId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.USER_ID
     *
     * @mbggenerated
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.METHOD
     *
     * @mbggenerated
     */
    private String method;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.REQ
     *
     * @mbggenerated
     */
    private String req;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.RSP
     *
     * @mbggenerated
     */
    private String rsp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.CREATE_TIME
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column INVOKE_LOG.UPDATE_TIME
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.ID
     *
     * @return the value of INVOKE_LOG.ID
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.ID
     *
     * @param id the value for INVOKE_LOG.ID
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.CONSUMER_ID
     *
     * @return the value of INVOKE_LOG.CONSUMER_ID
     *
     * @mbggenerated
     */
    public Long getConsumerId() {
        return consumerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.CONSUMER_ID
     *
     * @param consumerId the value for INVOKE_LOG.CONSUMER_ID
     *
     * @mbggenerated
     */
    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.USER_ID
     *
     * @return the value of INVOKE_LOG.USER_ID
     *
     * @mbggenerated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.USER_ID
     *
     * @param userId the value for INVOKE_LOG.USER_ID
     *
     * @mbggenerated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.METHOD
     *
     * @return the value of INVOKE_LOG.METHOD
     *
     * @mbggenerated
     */
    public String getMethod() {
        return method;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.METHOD
     *
     * @param method the value for INVOKE_LOG.METHOD
     *
     * @mbggenerated
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.REQ
     *
     * @return the value of INVOKE_LOG.REQ
     *
     * @mbggenerated
     */
    public String getReq() {
        return req;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.REQ
     *
     * @param req the value for INVOKE_LOG.REQ
     *
     * @mbggenerated
     */
    public void setReq(String req) {
        this.req = req == null ? null : req.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.RSP
     *
     * @return the value of INVOKE_LOG.RSP
     *
     * @mbggenerated
     */
    public String getRsp() {
        return rsp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.RSP
     *
     * @param rsp the value for INVOKE_LOG.RSP
     *
     * @mbggenerated
     */
    public void setRsp(String rsp) {
        this.rsp = rsp == null ? null : rsp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.CREATE_TIME
     *
     * @return the value of INVOKE_LOG.CREATE_TIME
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.CREATE_TIME
     *
     * @param createTime the value for INVOKE_LOG.CREATE_TIME
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column INVOKE_LOG.UPDATE_TIME
     *
     * @return the value of INVOKE_LOG.UPDATE_TIME
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column INVOKE_LOG.UPDATE_TIME
     *
     * @param updateTime the value for INVOKE_LOG.UPDATE_TIME
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}