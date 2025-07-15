package com.deer.business;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 业务模块启动类
 * 
 * <p>启用服务发现客户端，注册到 Eureka</p>
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.deer.framework",//   公共配置
        "com.deer.business",  //  business模块下所有包
})
@EnableFeignClients(basePackages = {
        "com.deer.systemApi",
})
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
        System.out.println("Business服务启动成功~");
    }
}