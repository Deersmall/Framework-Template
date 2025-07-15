package com.deer.framework.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * 加盐密码加密工具类
 * 使用PBKDF2WithHmacSHA256算法实现安全的密码存储
 */
public class EncryptUtils {

    // 算法参数配置
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;   // 迭代次数（越高越安全但越慢）
    private static final int KEY_LENGTH = 256;          // 生成的密钥长度（位）

    /**
     * 生成加盐加密后的密码
     *
     * @param password 原始密码（用户输入）
     * @param salt     盐值（推荐使用UUID字符串）
     * @return Base64编码的加密密码
     * @throws IllegalArgumentException 参数为空时抛出
     * @throws CryptoException 加密过程出错时抛出
     */
    public static String encrypt(String password, String salt) {
        // 参数校验
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty");
        }

        try {
            // 1. 创建密钥规范
            KeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt.getBytes(),
                    ITERATION_COUNT,
                    KEY_LENGTH
            );

            // 2. 获取密钥工厂实例
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);

            // 3. 生成密钥字节数组
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // 4. 返回Base64编码的字符串
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // 封装加密异常
            throw new CryptoException("Encryption failed", e);
        }
    }

    /**
     * 验证密码是否匹配
     *
     * @param inputPassword   用户输入的密码
     * @param salt            存储的盐值
     * @param storedPassword  存储的加密密码（Base64编码）
     * @return 匹配返回true，否则false
     */
    public static boolean verify(String inputPassword, String salt, String storedPassword) {
        // 生成输入密码的加密版本
        String encryptedInput = encrypt(inputPassword, salt);

        // 安全比较：防止时序攻击
        return constantTimeEquals(encryptedInput, storedPassword);
    }

    /**
     * 恒定时间比较算法（防止时序攻击）
     *
     * @param a 第一个字符串
     * @param b 第二个字符串
     * @return 相等返回true，否则false
     */
    private static boolean constantTimeEquals(String a, String b) {
        // 任意一个为空则直接返回不匹配
        if (a == null || b == null) {
            return false;
        }

        // 长度不同直接返回不匹配
        if (a.length() != b.length()) {
            return false;
        }

        int result = 0;
        // 恒定时间比较每个字符
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    /**
     * 自定义加密异常
     */
    public static class CryptoException extends RuntimeException {
        public CryptoException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}