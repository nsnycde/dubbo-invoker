package com.nsn.dubbo.dubboinvoker.service.enumeration;

/**
 * 描述:
 *
 * @author nsn
 */
public enum ReturnEnum {

    SUCCESS("000000", "成功"),
    FAIL("111111", "失败"),
    HOT_DEPLOY_FAIL("000002", "热部署失败"),

    EXCEPTION_USER_NAME_EXIST("300000", "用户名已存在"),
    EXCEPTION_USER_NOT_EXIST("300001", "用户不存在"),
    EXCEPTION_USER_PWD_ERROR("300002", "用户密码错误"),
    EXCEPTION_USER_DATA_ERROR("300003", "用户数据异常"),

    EXCEPTION_CONSUMER_NOT_EXIST("400000", "指定消费者不存在"),
    EXCEPTION_CONSUMER_EXIST("400001", "指定消费者已存在"),
    EXCEPTION_CONSUMER_NOT_UNIQUE("400002", "指定消费者不唯一"),
    EXCEPTION_CONSUMER_METHOD_NOT_EXIST("400003", "指定消费者方法不存在"),

    EXCEPTION_CONFIG_EXIST("500000", "指定配置已存在"),
    EXCEPTION_CONFIG_NOT_EXIST("500001", "指定配置不存在"),
    EXCEPTION_CONFIG_NOT_UNIQUE("500002", "指定配置不唯一"),
    EXCEPTION_DEPENDENCY_ERROR("500003", "请检查依赖信息正确性"),
    ;


    ReturnEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }}
