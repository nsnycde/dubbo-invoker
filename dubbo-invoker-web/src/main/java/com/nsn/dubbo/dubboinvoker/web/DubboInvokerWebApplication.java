package com.nsn.dubbo.dubboinvoker.web;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;

/**
 * @author nsn
 */
@Slf4j
@SpringBootApplication
public class DubboInvokerWebApplication extends SpringBootServletInitializer {

    static {
        setLogLevel();
    }

    private static void setLogLevel(){
        try {
            Method method = Logger.class.getDeclaredMethod("getLoggerContext");
            LoggerContext loggerContext = (LoggerContext)method.invoke(log);
            loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(Level.INFO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * main方法启动
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DubboInvokerWebApplication.class, args);
        log.info("当前进程pidInfo:{}", ManagementFactory.getRuntimeMXBean().getName());
    }

    /**
     * war包部署
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DubboInvokerWebApplication.class);
    }
}
