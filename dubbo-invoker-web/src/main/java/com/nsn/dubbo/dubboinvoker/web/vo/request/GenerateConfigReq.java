package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

/**
 * @author nsn
 */
@Data
public class GenerateConfigReq {

    private String configKey;
    private String configValue;
}
