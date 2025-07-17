package com.deer.framework.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthExceptions extends RuntimeException{

    // 错误码常量定义
    public static final int USER_NOT_FOUND = 10000;                  // 未找到用户
    public static final int PARAMETER_INVALID = 10001;               // 参数无效
    public static final int INCORRECT_ACCOUNT_PASSWORD = 10002;      // 账号密码错误
    public static final int ABNORMAL_ACCOUNT_STATUS = 10003;         // 账号状态异常
    public static final int LOGIN_AUTHENTICATION_EXPIRED = 10004;    // 登录认证过期
    public static final int AUTHENTICATION_FAILED = 10005;           // 认证失败
    public static final int PERMISSION_DENIED = 10006;               // 权限不足

    private Integer code;
    private String message;

    public AuthExceptions(String message)
    {
        this.message = message;
    }


}
