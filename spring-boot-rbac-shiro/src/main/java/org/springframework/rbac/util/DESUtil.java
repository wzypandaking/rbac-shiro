package org.springframework.rbac.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES加密工具类
 * Created by pandaking on 2017/7/27.
 */

/**
 * DES加密 解密算法
 *
 * @author lifq
 * @date 2015-3-17 上午10:12:11
 */
public class DESUtil {

    private final static String DES_IV = "20170727";
    private final static String DES = "DES";
    private final static String ENCODE = "UTF-8";

//    public static void main(String[] args) throws Exception {
//        String data = "测试ss";
//        String text = encrypt(data, "12345678");
//        System.out.println(text);
//        System.out.println(decrypt(text, "23456780"));
//
//    }
//

    public static String buildSecurityKey() {
        return RandomUtil.getCode(8, 3);
    }

    public static String encrypt(String encryptString, String securityKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes(ENCODE));
            SecretKeySpec key = new SecretKeySpec(securityKey.getBytes(ENCODE), DES);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return new BASE64Encoder().encode(encryptedData);
        } catch (Exception ignored) {

        }
        return "";
    }

    public static String decrypt(String decryptString, String securityKey) throws Exception {
        byte[] byteMi = new BASE64Decoder().decodeBuffer(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(DES_IV.getBytes(ENCODE));
        SecretKeySpec key = new SecretKeySpec(securityKey.getBytes(ENCODE), DES);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }
}