package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

/**
 * 描述: 查询配置请求
 *
 * @author nsn
 */
@Data
public class QueryConfigReq {

    private String configKey;
    private String configValue;
}
