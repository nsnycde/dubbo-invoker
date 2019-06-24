package com.nsn.dubbo.dubboinvoker.web.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author nsn
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConsumerRsp {

    private Long consumerId;
    private String consumerName;
}
