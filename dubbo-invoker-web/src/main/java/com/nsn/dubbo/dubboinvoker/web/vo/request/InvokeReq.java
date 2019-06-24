package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

import java.util.List;

/**
 * 描述: 调用请求
 *
 * @author nsn
 */
@Data
public class InvokeReq {

    private String consumerName;
    private String methodName;
    private List<List<String>> params;
}
