package com.deer.system.auth.service;


import com.deer.entities.system.SysUser;
import com.deer.entities.system.vo.LoginUser;

import java.util.Map;

public interface IAuthService {
    /**
     * 用户登录认证
     */
    Map<String, LoginUser> login(SysUser sysUser);
    
    /**
     * 用户登出
     */
    boolean logout();
}