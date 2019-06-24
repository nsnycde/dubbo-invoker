package com.nsn.dubbo.dubboinvoker.kit.convert;

/**
 * @author nsn
 * 类型转换接口
 */
public interface TypeConvert {

    /**
     * 类型转换
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    <T> T convert(String source, Class<T> targetClass);
}
