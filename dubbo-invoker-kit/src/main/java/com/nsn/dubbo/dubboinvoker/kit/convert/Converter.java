package com.nsn.dubbo.dubboinvoker.kit.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author nsn
 * 数据转换处理器上下文
 */
public class Converter {

    private final static Map<Class<?>,TypeConvert> TYPE_CONVERT_MAP;
    private final static List<Class<? extends TypeConvert>> TYPE_CONVERTS;
    static {
        TYPE_CONVERT_MAP = new HashMap<>();
        TYPE_CONVERTS = new ArrayList<>();
        //添加转换器
        TYPE_CONVERTS.add(SimpleTypeConvert.class);
        TYPE_CONVERTS.add(ExternalTypeConvert.class);
        //初始化
        init();
    }
    /**
     * 初始化数据转换Map
     */
    public static void init(){
        try {
            for(Class<? extends TypeConvert> convertClass : TYPE_CONVERTS){
                SourceType sourceType = convertClass.getDeclaredAnnotation(SourceType.class);
                if(sourceType == null
                        || sourceType.sourceTypes().length < 1){
                    continue;
                }
                TypeConvert typeConvert = convertClass.newInstance();
                for(Class<?> targetClass : sourceType.sourceTypes()){
                    TYPE_CONVERT_MAP.put(targetClass, typeConvert);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T convert(String source, Class<T> targetClass){
        try {
            //获取转换器
            TypeConvert convert = targetClass.isEnum() ? TYPE_CONVERT_MAP.get(Enum.class) : TYPE_CONVERT_MAP.get(targetClass);
            if(convert == null){
                return targetClass.newInstance();
            }
            return convert.convert(source, targetClass);
        } catch (Exception e){
            return null;
        }
    }

    public static boolean hasConvert(Class<?> targetClass){
        return targetClass.isEnum() ? TYPE_CONVERT_MAP.containsKey(Enum.class) : TYPE_CONVERT_MAP.containsKey(targetClass);
    }
}
