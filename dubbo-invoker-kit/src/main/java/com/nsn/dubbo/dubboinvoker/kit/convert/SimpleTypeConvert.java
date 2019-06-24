package com.nsn.dubbo.dubboinvoker.kit.convert;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author donghao
 * 基本类型转换
 */
@Slf4j
@SourceType(sourceTypes = {
        boolean.class,Boolean.class,
        char.class,Character.class,
        short.class,Short.class,
        byte.class,Byte.class,
        int.class,Integer.class,
        long.class,Long.class,
        float.class,Float.class,
        double.class,Double.class,
        Date.class, BigDecimal.class,
        String.class, Enum.class
})
public class SimpleTypeConvert implements TypeConvert{

    public <T> T convert(String source, Class<T> targetClass) {
        try {
            if(targetClass == null || source == null){
                return null;
            }
            if(targetClass == boolean.class || targetClass == Boolean.class){
                return (T)Boolean.valueOf(source);
            }
            if(targetClass == char.class || targetClass == Character.class){
                return (T)Character.valueOf(source.charAt(0));
            }
            if(targetClass == short.class || targetClass == Short.class){
                return (T)Short.valueOf(source);
            }
            if(targetClass == byte.class || targetClass == Byte.class){
                return (T)Byte.valueOf(source);
            }
            if(targetClass == int.class || targetClass == Integer.class){
                return (T)Integer.valueOf(source);
            }
            if(targetClass == long.class || targetClass == Long.class){
                return (T)Long.valueOf(source);
            }
            if(targetClass == float.class || targetClass == Float.class){
                return (T)Float.valueOf(source);
            }
            if(targetClass == double.class || targetClass == Double.class){
                return (T)Double.valueOf(source);
            }
            if(targetClass == Date.class){
                return (T)new SimpleDateFormat("yyyy-MM-dd").parse(source);
            }
            if(targetClass == BigDecimal.class){
                return (T)new BigDecimal(source);
            }
            if(targetClass == String.class){
                return (T)source;
            }
            if(targetClass.isEnum()){
                Class enumClass = targetClass;
                return (T)Enum.valueOf(enumClass, source);
            }
        } catch (Exception e) {
            log.error("类型转换异常!source:{},targetClass:{}", source, targetClass);
            throw new RuntimeException("类型转换异常!source:" + source + ",targetClass:" + targetClass);
        }
        log.error("无对应的转换类型!source:{},targetClass:{}", source, targetClass);
        throw new RuntimeException("无对应的转换类型!source:" + source + ",targetClass:" + targetClass);
    }
}