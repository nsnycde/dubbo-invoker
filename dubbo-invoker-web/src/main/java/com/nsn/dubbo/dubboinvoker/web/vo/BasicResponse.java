package com.nsn.dubbo.dubboinvoker.web.vo;

import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import lombok.Data;

/**
 * @author nsn
 * @param <T>
 */
@Data
public class BasicResponse<T> {

    private String code;

    private String msg;

    private T data;

    public BasicResponse() {
        this.code = ReturnEnum.SUCCESS.getCode();
        this.msg = ReturnEnum.SUCCESS.getMsg();
    }

    public BasicResponse(T data, String code, String message) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    public BasicResponse(T data, ReturnEnum returnEnum) {
        this.code = returnEnum.getCode();
        this.msg = returnEnum.getMsg();
        this.data = data;
    }

    public void setCodeAndMsg(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public void setCodeAndMsg(BizException e){
        this.code = e.getCode();
        this.msg = e.getMsg();
    }

    public void setCodeAndMsg(ReturnEnum returnEnum){
        this.code = returnEnum.getCode();
        this.msg = returnEnum.getMsg();
    }
}
