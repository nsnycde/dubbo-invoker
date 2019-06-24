package com.nsn.dubbo.dubboinvoker.service.exception;

import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import lombok.Data;

/**
 * 描述: 业务异常
 *
 * @author nsn
 */
@Data
public class BizException extends RuntimeException{

    private String code;
    private String msg;

    public BizException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException(ReturnEnum returnEnum) {
        this.code = returnEnum.getCode();
        this.msg = returnEnum.getMsg();
    }
}
