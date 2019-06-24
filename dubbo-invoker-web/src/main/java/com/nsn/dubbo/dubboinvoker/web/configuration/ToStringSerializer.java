package com.nsn.dubbo.dubboinvoker.web.configuration;

import com.alibaba.fastjson.serializer.ValueFilter;

/**
 * 描述:
 *
 * @author donghao
 * @date 2019-02-19 16:12
 */
public class ToStringSerializer implements ValueFilter {

    private final static long LIMIT = 10000000000000000L;

    @Override
    public Object process(Object object, String name, Object value) {
        if(value instanceof Long) {
            if((Long) value >= LIMIT) {
                return String.valueOf(value);
            }
        }
        return value;
    }
}
