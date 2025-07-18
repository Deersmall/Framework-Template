package com.deer.framework.utils;

import com.deer.entities.system.vo.LoginUser;
import com.deer.framework.exception.GlobalGenericExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 *
 * @author soar
 */
public class SecurityUtils
{
    /**
     * 用户ID
     **/
    public static String getUserId() {
        try {
            return getLoginUser().getSysUser().getUserId();
        }
        catch (Exception e) {
            throw new GlobalGenericExceptions(HttpStatus.UNAUTHORIZED.value(),"获取用户ID异常" );
        }
    }


    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        }
        catch (Exception e) {
            throw new GlobalGenericExceptions(HttpStatus.UNAUTHORIZED.value(),"获取用户账户异常" );
        }
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e) {
            throw new GlobalGenericExceptions(HttpStatus.UNAUTHORIZED.value(),"获取用户信息异常" );
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword 真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
