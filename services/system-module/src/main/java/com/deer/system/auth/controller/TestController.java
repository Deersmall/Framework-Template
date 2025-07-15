package com.deer.system.auth.controller;

import com.deer.entities.system.SysUser;
import com.deer.system.auth.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 
 * <p>提供一个简单的 GET 接口用于验证框架是否正常运行</p>
 */
@RestController
@RequestMapping("/sysTest")
public class TestController {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 测试接口
     * 
     * @return 简单的响应消息
     */
    @PostMapping("/sysTestGetUser")
    @PreAuthorize("hasAuthority('test')")
    public SysUser sysTestGetUser(@RequestParam("userId") String userId) {
        return sysUserMapper.selectById(userId);
    }
}