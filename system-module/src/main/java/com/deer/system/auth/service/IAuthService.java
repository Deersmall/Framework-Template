package com.deer.system.auth.service;

import com.deer.base.security.entity.SysUser;

import java.util.Map;

public interface IAuthService {
    /**
     * 用户登录认证
     */
    Map<String, String> login(SysUser sysUser);
    
    /**
     * 用户登出
     */
    boolean logout();
}