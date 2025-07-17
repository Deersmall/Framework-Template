package com.deer.framework.utils;

public class BaseController extends SpringContextUtil{
    protected <TService> TService ref(Class<TService> clazz) {
        TService bean = (TService)super.getBean(clazz);
        //获取ioc容器
//        ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取ioc容器中的bean
//        TService bean = ioc.getBean(clazz);
        return bean;
    }



}