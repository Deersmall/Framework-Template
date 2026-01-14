package com.deer.framework.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * 加盐密码加密工具类
 * 使用PBKDF2WithHmacSHA256算法实现安全的密码存储
 * 新增AES-GCM对称加密用于需要解密的场景，支持UUID作为密钥
 */
public class EncryptUtils {

    // PBKDF2算法参数配置
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;   // 迭代次数（越高越安全但越慢）
    private static final int KEY_LENGTH = 256;          // 生成的密钥长度（位）

    // AES-GCM算法参数配置
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 256;        // AES密钥长度
    private static final int GCM_TAG_LENGTH = 128;      // GCM认证标签长度
    private static final int GCM_IV_LENGTH = 12;        // GCM初始向量长度（字节）

    /**
     * 生成加盐加密后的密码（单向哈希，不可逆）
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
     * 从UUID生成AES密钥（256位）
     * 使用SHA-256哈希将任意长度的UUID转换为固定256位密钥
     *
     * @param uuidString UUID字符串（可以带或不带连字符）
     * @return AES密钥对象
     */
    private static SecretKey generateAesKeyFromUuid(String uuidString) {
        try {
            // 移除UUID中的连字符（如果有）
            String normalizedUuid = uuidString.replace("-", "");

            // 使用SHA-256哈希生成固定长度的密钥
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(normalizedUuid.getBytes());

            // 确保密钥长度为256位（32字节）
            if (keyBytes.length != 32) {
                byte[] truncatedKey = new byte[32];
                System.arraycopy(keyBytes, 0, truncatedKey, 0, Math.min(keyBytes.length, 32));
                keyBytes = truncatedKey;
            }

            return new SecretKeySpec(keyBytes, "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Failed to generate AES key from UUID", e);
        }
    }

    /**
     * AES-GCM对称加密（可逆）
     *
     * @param plaintext 明文
     * @param uuidKey   UUID字符串作为密钥
     * @return Base64编码的密文（格式：IV + 密文）
     * @throws IllegalArgumentException 参数为空时抛出
     * @throws CryptoException 加密过程出错时抛出
     */
    public static String aesEncrypt(String plaintext, String uuidKey) {
        if (plaintext == null || plaintext.isEmpty()) {
            throw new IllegalArgumentException("Plaintext cannot be null or empty");
        }
        if (uuidKey == null || uuidKey.isEmpty()) {
            throw new IllegalArgumentException("UUID key cannot be null or empty");
        }

        try {
            // 1. 生成随机IV（初始向量）
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(iv);

            // 2. 从UUID生成AES密钥
            SecretKey key = generateAesKeyFromUuid(uuidKey);

            // 3. 初始化Cipher
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

            // 4. 执行加密
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

            // 5. 组合IV和密文
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

            // 6. 返回Base64编码结果
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new CryptoException("AES encryption failed", e);
        }
    }

    /**
     * AES-GCM对称解密
     *
     * @param ciphertext Base64编码的密文（格式：IV + 密文）
     * @param uuidKey    UUID字符串作为密钥（与加密时相同）
     * @return 解密后的明文
     * @throws IllegalArgumentException 参数为空或格式错误时抛出
     * @throws CryptoException 解密过程出错时抛出
     */
    public static String aesDecrypt(String ciphertext, String uuidKey) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            throw new IllegalArgumentException("Ciphertext cannot be null or empty");
        }
        if (uuidKey == null || uuidKey.isEmpty()) {
            throw new IllegalArgumentException("UUID key cannot be null or empty");
        }

        try {
            // 1. Base64解码
            byte[] combined = Base64.getDecoder().decode(ciphertext);

            // 2. 分离IV和密文
            if (combined.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Invalid ciphertext format");
            }

            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encryptedContent = new byte[combined.length - GCM_IV_LENGTH];

            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, encryptedContent, 0, encryptedContent.length);

            // 3. 从UUID生成AES密钥
            SecretKey key = generateAesKeyFromUuid(uuidKey);

            // 4. 初始化Cipher
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

            // 5. 执行解密
            byte[] plaintextBytes = cipher.doFinal(encryptedContent);
            return new String(plaintextBytes);
        } catch (Exception e) {
            throw new CryptoException("AES decryption failed", e);
        }
    }

    /**
     * 验证密码是否匹配（单向哈希验证）
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