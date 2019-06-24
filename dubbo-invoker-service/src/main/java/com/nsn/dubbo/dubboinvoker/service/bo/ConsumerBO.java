package com.nsn.dubbo.dubboinvoker.service.bo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.nsn.dubbo.dubboinvoker.kit.ClassLoaderKit;
import lombok.Data;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 *
 * @author nsn
 */
@Data
public class ConsumerBO {

    private final static ApplicationConfig APPLICATION_CONFIG = new ApplicationConfig("dubboInvoker");

    /**
     * method 参数 simpleName
     */
    private final static boolean IS_SIMPLE = true;

    private final static Field INIT_FLAG_FIELD;

    static {
        try {
            INIT_FLAG_FIELD = ReferenceConfig.class.getDeclaredField("initialized");
            INIT_FLAG_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new Error("INIT_FLAG_FIELD获取失败!");
        }
    }

    private Long consumerId;
    /**
     * 是否已经初始化
     */
    private volatile Boolean init;
    private String name;
    private String interfaceName;
    private String url;
    private String group;
    private String version;
    private Class<?> interfaceClass;
    private Map<String, Method> methodMap;
    /**
     * 代理对象
     */
    private ReferenceConfig<?> referenceConfig;

    /**
     * 构造ConsumerBo
     * @param name url@group#version
     * @param init 是否执行初始化
     * @return
     */
    public static ConsumerBO parseName(String name, boolean init){
        Assert.hasText(name, "name不能为空!");
        ConsumerBO consumerBO = new ConsumerBO();
        consumerBO.setName(name);
        int groupIndex = name.indexOf("@");
        int versionIndex = name.indexOf("#");
        if(groupIndex == versionIndex){
            consumerBO.setUrl(name);
        }else {
            if(versionIndex != -1){
                consumerBO.setVersion(name.substring(versionIndex));
                if(groupIndex != -1){
                    consumerBO.setGroup(name.substring(groupIndex, versionIndex));
                }
            }else {
                consumerBO.setGroup(name.substring(groupIndex));
            }
        }
        consumerBO.setInterfaceName(consumerBO.getUrl().substring(consumerBO.getUrl().lastIndexOf("/")+1));
        //接口class
        consumerBO.setInterfaceClass(ClassLoaderKit.forName(consumerBO.getInterfaceName()));
        Method[] methodArray = consumerBO.getInterfaceClass().getMethods();
        consumerBO.setMethodMap(new HashMap<>(methodArray.length));
        //方法列表
        for(Method method : methodArray){
            method.setAccessible(false);
            StringBuilder methodKeyBuilder = new StringBuilder(method.getName()).append("(");
            Class<?>[] paramClassArray = method.getParameterTypes();
            Type[] genericArray = method.getGenericParameterTypes();
            for(int i=0;i<paramClassArray.length;i++){
                if(IS_SIMPLE){
                    methodKeyBuilder.append(paramClassArray[i].getSimpleName()).append(", ");
                }else {
                    methodKeyBuilder.append(genericArray[i].getTypeName()).append(", ");
                }
            }
            methodKeyBuilder.delete(methodKeyBuilder.length()-2, methodKeyBuilder.length()).append(")");
            consumerBO.getMethodMap().put(methodKeyBuilder.toString(), method);
        }

        //构造referenceConfig
        ReferenceConfig<?> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(APPLICATION_CONFIG);
        referenceConfig.setUrl(consumerBO.getUrl());
        referenceConfig.setGroup(consumerBO.getGroup());
        referenceConfig.setVersion(consumerBO.getVersion());
        referenceConfig.setInterface(consumerBO.getInterfaceClass());
        referenceConfig.setTimeout(10000);
        consumerBO.setReferenceConfig(referenceConfig);

        //初始化消费者
        if(init){
            consumerBO.setInit(true);
            //暂存当前loader
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(ClassLoaderKit.getClassLoader());
            consumerBO.getReferenceConfig().get();
            Thread.currentThread().setContextClassLoader(loader);
        }
        return consumerBO;
    }

    /**
     * 构造name
     * @param url
     * @param group
     * @param version
     * @return
     */
    public static String buildName(String url, String group, String version){
        Assert.hasText(url, "url不能为空!");
        StringBuilder nameBuilder = new StringBuilder(url.trim());
        if(!StringUtils.isEmpty(group)){
            nameBuilder.append("@").append(group);
        }
        if(!StringUtils.isEmpty(version)){
            nameBuilder.append("#").append(version);
        }
        return nameBuilder.toString();
    }

    /**
     * 判断是否init
     * @return
     */
    public boolean isInit() {
        try {
            return (boolean)INIT_FLAG_FIELD.get(this.getReferenceConfig());
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
