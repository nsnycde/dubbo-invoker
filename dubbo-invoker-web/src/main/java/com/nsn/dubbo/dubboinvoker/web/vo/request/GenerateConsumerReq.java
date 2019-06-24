package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

/**
 * @author nsn
 */
@Data
public class GenerateConsumerReq {

    private String url;
    private String group;
    private String version;
}
