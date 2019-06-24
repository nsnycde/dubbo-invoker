package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

/**
 * 描述: 查询表头请求
 *
 * @author nsn
 */
@Data
public class QueryTableHeaderReq {

    private String consumerName;
    private String methodName;
}
