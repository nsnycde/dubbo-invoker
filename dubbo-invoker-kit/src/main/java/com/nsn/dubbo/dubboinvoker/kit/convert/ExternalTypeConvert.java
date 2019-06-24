package com.nsn.dubbo.dubboinvoker.kit.convert;

import lombok.extern.slf4j.Slf4j;

/**
 * @author donghao
 * 额外的类型转换器
 */
@Slf4j
@SourceType(sourceTypes = {

})
public class ExternalTypeConvert implements TypeConvert{


    public <T> T convert(String source, Class<T> targetClass) {
        log.error("无对应的转换类型!source:{},targetClass:{}", source, targetClass);
        throw new RuntimeException("无对应的转换类型!source:" + source + ",targetClass:" + targetClass);
    }
}
