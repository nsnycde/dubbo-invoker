package com.nsn.dubbo.dubboinvoker.web.vo.request;

import lombok.Data;

/**
 * 描述: 删除配置
 *
 * @author nsn
 */
@Data
public class DeleteConfigReq {

    private Long configId;
    private String configKey;
}
