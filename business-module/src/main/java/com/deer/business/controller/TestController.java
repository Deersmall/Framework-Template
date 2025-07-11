package com.deer.business.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 
 * <p>提供一个简单的 GET 接口用于验证框架是否正常运行</p>
 */
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 测试接口
     * 
     * @return 简单的响应消息
     */
    @GetMapping("/hello")
    public String hello() {
        System.out.println(System.currentTimeMillis());
        return "Business module is running! Current time: " + System.currentTimeMillis();
    }
}