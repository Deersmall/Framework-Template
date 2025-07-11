package com.deer.base.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * TODO
 *
 * @author CSD
 * @date 2022-07-28 14:16
 */
public class AESUtils {
 
    //密钥 (需要前端和后端保持一致)十六位作为密钥
    private static final String KEY = "Deer_Small_85866";
 
    //密钥偏移量 (需要前端和后端保持一致)十六位作为密钥偏移量
    private static final String IV = "Deer_Small_85800";
 
    //算法
    private static final String ALGORITHMSTR = "AES/CBC/PKCS5Padding";
 
    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null :  Base64.getDecoder().decode(base64Code);
    }
 
    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes) throws Exception {
 
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
 
        byte[] temp = IV.getBytes("UTF-8");
        IvParameterSpec iv = new IvParameterSpec(temp);
 
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(), "AES"), iv);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
 
        System.out.print(new String(decryptBytes));
        return new String(decryptBytes);
    }
 
    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @return 解密后的string
     */
    public static String aesDecrypt(String encryptStr){
        String aesDecryptStr = "";
        if (StringUtils.isEmpty(encryptStr)) return aesDecryptStr;
        try {
            aesDecryptStr = StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aesDecryptStr;
    }
 
//    //测试一下
//    public static void main(String[] args) throws Exception {
//        String str = "Q uus tQvLdwtGSldhrtKQ==";
//        str = str.replace(" ", "+");
//        System.out.println(str);
//        aesDecrypt(str);
//    }
}
 