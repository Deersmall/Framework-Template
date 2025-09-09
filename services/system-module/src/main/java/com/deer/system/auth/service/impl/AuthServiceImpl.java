package com.deer.system.auth.service.impl;


import com.deer.entities.system.SysUser;
import com.deer.entities.system.vo.LoginUser;
import com.deer.framework.exception.AuthExceptions;
import com.deer.framework.utils.EncryptUtils;
import com.deer.framework.utils.JwtUtils;
import com.deer.framework.utils.RedisUtils;
import com.deer.system.auth.service.IAuthService;
import com.deer.system.sysUser.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

import static com.deer.framework.constants.RedisConstants.LOGIN_USER;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;


    @Value("${jwt.expiration}")
    private long expiration;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RedisUtils redisUtils ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Autowired
    private ISysUserService ISysUserService;

    @Override
    public Map<String, LoginUser> login(SysUser sysUser) {
//        查询用户
        SysUser sysUserByName = ISysUserService.sysUserByUserName(sysUser.getUserName());

        //  密码加盐加密
        sysUser.setPassword(EncryptUtils.encrypt(sysUser.getPassword(), sysUserByName.getSalt()));

        // 1. 创建认证对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        sysUser.getUserName(),
                        sysUser.getPassword());

        // 2. 完成执行认证
        try {
            // 正确使用认证管理器
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            //  认证校验
            if (ObjectUtils.isEmpty(authentication))
                throw  new AuthExceptions(AuthExceptions.AUTHENTICATION_FAILED,"认证失败");

            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            Map<String, LoginUser> map = new HashMap<>();


            // 3. 生成JWT令牌
            String token = jwtUtils.generateToken(loginUser.getUsername());

            loginUser.setToken(token);
            map.put("loginUserInfo",loginUser);

            // 4. 将令牌存储到Redis
            redisUtils.set(LOGIN_USER + loginUser.getUsername(),loginUser, 300);

            // 5. 返回认证结果
//            return new AuthVO(token, "LoginUserToken", 30l);
            return map;

        } catch (Exception e){
            if (e instanceof BadCredentialsException) {
                throw new AuthExceptions(AuthExceptions.INCORRECT_ACCOUNT_PASSWORD,"账号密码错误");
            } else {
                throw new AuthExceptions(AuthExceptions.AUTHENTICATION_FAILED,e.getMessage());
            }
        }

    }

    @Override
    public boolean logout() {

        //  获取登录认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //  从Redis删除令牌
        Boolean isDelete = redisUtils.delete(LOGIN_USER + loginUser.getUsername());

        //  清除Security上下文
        SecurityContextHolder.clearContext();

        return isDelete;
    }
}