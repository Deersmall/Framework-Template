package com.deer.business.controller;

import com.deer.entities.system.SysUser;
import com.deer.systemApi.auth.ISystemApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Autowired
    private ISystemApi iSystemApi;

    /**
     * 测试接口
     * 
     * @return 简单的响应消息
     */
    @PostMapping("/hello")
    public SysUser hello() {

        return iSystemApi.sysTestGetUser("U-Admin");
    }
}