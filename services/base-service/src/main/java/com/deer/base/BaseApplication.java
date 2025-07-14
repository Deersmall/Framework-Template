package com.deer.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 基础服务启动类
 *
 * 访问控制台：http://localhost:8761/
 * 健康检查端点：http://localhost:8761/actuator/health
 * <p>同时作为 Eureka 注册中心和公共配置中心</p>
 */

@SpringBootApplication
@EnableEurekaServer
@ComponentScan(basePackages = {"com.deer.base.eureka"})//   仅扫描eureka的配置类
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
        System.out.println("Base服务启动成功~");
    }
}