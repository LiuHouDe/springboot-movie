package com.ispan.theater.util;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricKeyGeneratorUtil {
    public static String generateBase64SecretKey() throws NoSuchAlgorithmException {
        // 使用HmacSHA256算法生成對稱密鑰
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256); // 指定密鑰大小，這裡使用256位
        SecretKey secretKey = keyGen.generateKey();

        // 將密鑰進行Base64編碼
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
