package com.nsn.dubbo.dubboinvoker.kit;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 类加载器工具
 * @author nsn
 */
@Slf4j
public final class ClassLoaderKit {

    /**
     * urlClassLoader 加载 指定 jar
     */
    private static volatile MyLoader myLoader;

    /**
     * 加载类
     * @param className
     * @return
     */
    public static Class<?> forName(String className){
        try {
            Class<?> clz = basicClass(className);
            return clz == null ? Class.forName(className, true, getClassLoader()) : clz;
        } catch (Exception e) {
            log.error("加载类出错,className:{}", className);
            throw new RuntimeException(e);
        }
    }

    /**
     * 基本类型
     * @param basicName
     * @return
     */
    public static Class<?> basicClass(String basicName){
        if(byte.class.getName().equals(basicName)){
            return byte.class;
        }else if(char.class.getName().equals(basicName)){
            return char.class;
        }else if(boolean.class.getName().equals(basicName)){
            return boolean.class;
        }else if(short.class.getName().equals(basicName)){
            return short.class;
        }else if(int.class.getName().equals(basicName)){
            return int.class;
        }else if(long.class.getName().equals(basicName)){
            return long.class;
        }else if(float.class.getName().equals(basicName)){
            return float.class;
        }else if(double.class.getName().equals(basicName)){
            return double.class;
        }
        return null;
    }

    /**
     * 获取classLoader
     * @return
     */
    public static ClassLoader getClassLoader(){
        if(myLoader == null){
            return Thread.currentThread().getContextClassLoader();
        }
        return myLoader;
    }

    /**
     * 添加jar到myClassLoader下加载
     * @param jarFile
     * @return
     */
    public static ClassLoader addJar(File jarFile){
        if(!jarFile.exists()
                || !jarFile.isFile()
                || !jarFile.getName().endsWith(".jar")){
            log.error("jar路径异常,jarPath:{}", jarFile.getAbsolutePath());
            throw new RuntimeException("jar路径异常,jarPath:{}" + jarFile.getAbsolutePath());
        }
        if(myLoader == null){
            synchronized (ClassLoaderKit.class){
                if(myLoader == null){
                    try {
                        log.info("添加jar:{}", jarFile.getAbsolutePath());
                        myLoader = new MyLoader(new URL[]{jarFile.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                        log.info("myLoader.parent:{}", myLoader.getParent());
                        return myLoader;
                    } catch (MalformedURLException e) {
                        log.error("创建urlClassLoader异常:", e);
                        throw new RuntimeException("创建urlClassLoader异常", e);
                    }
                }
            }
        }
        try {
            log.info("添加jar:{}", jarFile.getAbsolutePath());
            myLoader.addURL(jarFile.toURI().toURL());
            return myLoader;
        } catch (Exception e) {
            log.error("加载jar异常,jarPath:{}", jarFile.getAbsolutePath());
            throw new RuntimeException("加载jar异常,jarPath:{}" + jarFile.getAbsolutePath());
        }
    }

    /**
     * 添加jar到myClassLoader下加载
     * @param jarPath
     * @return
     */
    public static ClassLoader addJar(String jarPath){
        return addJar(new File(jarPath));
    }

    /**
     * 自定义类加载器
     */
    public static class MyLoader extends URLClassLoader {
        private MyLoader(URL[] urls, ClassLoader parent) {
            super(urls, parent);
        }

        @Override
        public void close() throws IOException {
            super.close();
        }

        @Override
        protected void addURL(URL url) {
            super.addURL(url);
        }
    }

    /**
     *  清除loader
     * @return
     */
    public static MyLoader clearLoader(){
        MyLoader loader = myLoader;
        myLoader = null;
        return loader;
    }
}
