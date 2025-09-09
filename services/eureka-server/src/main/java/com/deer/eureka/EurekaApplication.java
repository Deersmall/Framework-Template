package com.deer.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka注册中心启动类
 *
 * 访问控制台：http://localhost:8761/
 * 健康检查端点：http://localhost:8761/actuator/health
 * <p>Eureka 注册中心</p>
 */

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
        System.out.println("Eureka服务启动成功~");
    }
}