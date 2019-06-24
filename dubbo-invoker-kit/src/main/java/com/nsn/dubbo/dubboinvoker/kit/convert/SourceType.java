package com.nsn.dubbo.dubboinvoker.kit.convert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author nsn
 *  数据转换源类型
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceType {
    Class<?>[] sourceTypes();
}
