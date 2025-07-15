package com.deer.framework.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    public SpringContextUtil() {
    }
    /**
     * 设置上下文
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext == null) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }
    /**
     * 获取上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * 通过名字获取上下文中的bean
     * @param name
     * @return
     */
    public Object getBean(String name){
        return applicationContext.getBean(name);
    }
    /**
     * 通过类型获取上下文中的bean
     * @param requiredType
     * @return
     */
    public Object getBean(Class<?> requiredType){
        return applicationContext.getBean(requiredType);
    }
}