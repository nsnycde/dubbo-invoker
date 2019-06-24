package com.nsn.dubbo.dubboinvoker.web.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nsn
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConfigRsp {

    private Long configId;
    private String configKey;
    private String configValue;
}
