package com.nsn.dubbo.dubboinvoker.service.core.impl;

import com.alibaba.fastjson.JSON;
import com.nsn.dubbo.dubboinvoker.kit.ClassLoaderKit;
import com.nsn.dubbo.dubboinvoker.kit.convert.Converter;
import com.nsn.dubbo.dubboinvoker.service.InvokeLogService;
import com.nsn.dubbo.dubboinvoker.service.bo.ConsumerBO;
import com.nsn.dubbo.dubboinvoker.service.core.InvokeService;
import com.nsn.dubbo.dubboinvoker.service.enumeration.ReturnEnum;
import com.nsn.dubbo.dubboinvoker.service.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 描述: 默认的调用逻辑实现
 *
 * @author nsn
 */
@Slf4j
@Component
public class DefaultInvokeServiceImpl implements InvokeService {

    @Resource
    private InvokeLogService invokeLogService;
    @Resource
    private DataParser parser;

    @Override
    public Object invoke(Long userId, ConsumerBO consumerBO, String methodName, List<List<String>> params) {
        if(consumerBO == null){
            throw new BizException(ReturnEnum.EXCEPTION_CONSUMER_NOT_EXIST);
        }
        //获取对应的方法
        Method method = consumerBO.getMethodMap().get(methodName);
        //泛型参数列表
        Type[] genericTypes = method.getGenericParameterTypes();
        //获取参数列表
        Parameter[] parameters = method.getParameters();
        List<Object> paramValues;
        paramValues = parser.parseRow(params);
        log.info("excelValue:{}", JSON.toJSONString(paramValues));
        //方法参数值数组
        Object[] methodParams = parameters.length <= 0 ? null : new Object[parameters.length];
        for(int i=0;i<parameters.length;i++){
            //参数
            Parameter param = parameters[i];
            //参数对应
            Object paramValue = null;
            if(paramValues != null){
                paramValue = paramValues.size() > i ? paramValues.get(i) : null;
            }
            //解析参数
            methodParams[i] = this.parseData(param.getType(), paramValue, genericTypes[i].getTypeName());
        }
        log.info("methodParams:{}", JSON.toJSONString(methodParams));
        //方法调用
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if(!consumerBO.isInit()){
                Thread.currentThread().setContextClassLoader(ClassLoaderKit.getClassLoader());
            }
            Object result = method.invoke(consumerBO.getReferenceConfig().get(), methodParams);
            Thread.currentThread().setContextClassLoader(loader);
            invokeLogService.addLog(methodName, methodParams, result, consumerBO.getConsumerId(), userId);
            log.info("rpcResult:{}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            throw new RuntimeException("调用dubbo服务出错!", e);
        }
    }


    /**
     * 数据转换
     * @param clz
     * @param params
     * @param genericInfo
     * @return
     */
    private Object parseData(Class<?> clz, Object params, String genericInfo){
        if(params == null){
            return null;
        }
        try {
            //基本类型
            if(Converter.hasConvert(clz)){
                return Converter.convert(params.toString(), clz);
            }else if(clz.isAssignableFrom(List.class)){
                //List类型
                List paramsList = (List)params;
                String genericClassName = genericInfo.substring(genericInfo.indexOf("<") + 1, genericInfo.indexOf(">"));
                Class<?> genericClass = ClassLoaderKit.forName(genericClassName);
                List<Object> valueList = new ArrayList<>();
                for(Object obj : paramsList){
                    Object value = this.parseData(genericClass, obj, null);
                    valueList.add(value);
                }
                return valueList;
            }else {
                //Bean类型
                Object beanObj = clz.newInstance();
                List<Field> fields = new ArrayList<>();
                //先添加父类字段，再添加当前类字段
                fields.addAll(Arrays.asList(clz.getSuperclass().getDeclaredFields()));
                fields.addAll(Arrays.asList(clz.getDeclaredFields()));
                for(Field field : fields) {
                    if (Modifier.isFinal(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object value = this.parseData(field.getType(), ((Map)params).get(field.getName()), field.toGenericString());
                    field.set(beanObj, value);
                }
                return beanObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
