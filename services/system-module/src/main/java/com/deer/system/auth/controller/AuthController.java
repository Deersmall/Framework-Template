package com.deer.system.auth.controller;

import com.deer.entities.system.SysUser;
import com.deer.framework.result.CommonResult;
import com.deer.system.auth.service.IAuthService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录接口
     */
    @SneakyThrows
    @PostMapping("/login")
    public CommonResult login(@RequestBody SysUser sysUser) {
        return CommonResult.ok(authService.login(sysUser));
//        return authService.login(sysUser);
    }

    /**
     * 用户登出接口
     */
    @PostMapping("/logout")
    public CommonResult logout() {
        return new CommonResult(authService.logout());
    }
}