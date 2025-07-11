package com.deer.base.common.exception;

/**
 * 全局通用异常类
 * 
 * 设计原则：
 * 1. 继承RuntimeException（非受检异常）
 * 2. 支持错误码机制（便于国际化处理）
 * 3. 携带原始异常信息（保留堆栈跟踪）
 * 4. 支持参数化错误消息（使用{}占位符）
 */
public class GlobalGenericExceptions extends RuntimeException {
    
    // 错误码常量定义（可根据业务扩展）
    public static final int UNKNOWN_ERROR = 1000;            // 未知错误
    public static final int PARAMETER_INVALID = 1001;        // 参数无效
    public static final int AUTHENTICATION_FAILED = 2001;    // 认证失败
    public static final int PERMISSION_DENIED = 2002;        // 权限不足
    public static final int RESOURCE_NOT_FOUND = 3001;       // 资源不存在
    public static final int SERVICE_UNAVAILABLE = 5001;      // 服务不可用
    public static final int DATABASE_ERROR = 5002;           // 数据库错误

    private final int errorCode;  // 错误码
    private final Object[] args;  // 错误消息参数

    /**
     * 完整构造函数
     * @param errorCode 错误码
     * @param message 错误消息模板（支持{}占位符）
     * @param args 消息模板参数
     * @param cause 原始异常
     */
    public GlobalGenericExceptions(int errorCode, String message, Object[] args, Throwable cause) {
        super(formatMessage(message, args), cause);
        this.errorCode = errorCode;
        this.args = args != null ? args.clone() : null;
    }

    // ========== 简化构造函数 ========== 
    
    public GlobalGenericExceptions(int errorCode, String message) {
        this(errorCode, message, null, null);
    }
    
    public GlobalGenericExceptions(int errorCode, String message, Throwable cause) {
        this(errorCode, message, null, cause);
    }
    
    public GlobalGenericExceptions(int errorCode, String message, Object... args) {
        this(errorCode, message, args, null);
    }
    
    // ========== 工厂方法（推荐使用） ==========
    
    public static GlobalGenericExceptions of(int errorCode, String message) {
        return new GlobalGenericExceptions(errorCode, message);
    }
    
    public static GlobalGenericExceptions of(int errorCode, String message, Throwable cause) {
        return new GlobalGenericExceptions(errorCode, message, cause);
    }
    
    public static GlobalGenericExceptions of(int errorCode, String message, Object... args) {
        return new GlobalGenericExceptions(errorCode, message, args);
    }

    // ========== 常用异常快捷创建方法 ==========
    
    public static GlobalGenericExceptions paramInvalid(String message, Object... args) {
        return new GlobalGenericExceptions(PARAMETER_INVALID, message, args);
    }
    
    public static GlobalGenericExceptions authFailed(String message) {
        return new GlobalGenericExceptions(AUTHENTICATION_FAILED, message);
    }
    
    public static GlobalGenericExceptions notFound(String resourceName) {
        return new GlobalGenericExceptions(RESOURCE_NOT_FOUND, "资源不存在: {}", resourceName);
    }
    
    public static GlobalGenericExceptions serviceUnavailable() {
        return new GlobalGenericExceptions(SERVICE_UNAVAILABLE, "服务暂时不可用，请稍后重试");
    }

    // ========== 工具方法 ==========
    
    /**
     * 格式化带占位符的错误消息
     */
    private static String formatMessage(String template, Object... args) {
        if (args == null || args.length == 0) {
            return template;
        }
        String formatted = template;
        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{}", arg.toString());
        }
        return formatted;
    }

    // ========== Getter方法 ==========
    
    public int getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args != null ? args.clone() : null;
    }

    // ========== 覆盖方法 ==========
    
    @Override
    public String toString() {
        return "BusinessException{" +
                "errorCode=" + errorCode +
                ", message=" + getMessage() +
                ", cause=" + (getCause() != null ? getCause().getMessage() : "null") +
                '}';
    }
}