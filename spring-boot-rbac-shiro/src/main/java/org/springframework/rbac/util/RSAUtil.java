package org.springframework.rbac.util;

/**
 * @author: 王书远<shuyuan.wang@tqmall.com>
 * @create: 2017/5/18
 * @note: 请补充说明
 * @history:
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

public class RSAUtil {


    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    public static String encrypt(Map<String, Object> param, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(JSON.toJSONBytes(param)));
    }

    public static byte[] decrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

}