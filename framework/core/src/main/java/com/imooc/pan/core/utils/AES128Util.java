package com.imooc.pan.core.utils;

import cn.hutool.core.codec.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AES128Util {

    /**
     * default
     */
    public static final String IV = "akjsfakjshf@#!~&";

    /**
     * key
     */
    private static final String P_KEY = StringUtils.reverse(IV);

    private static final String AES_STR = "AES";
    private static final String INSTANCE_STR = "AES/CBC/PKCS5Padding";

    /**
     * encrypting bytes
     *
     * @param content bytes before encryption
     * @return encrypted
     */
    public static byte[] aesEncrypt(byte[] content) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(P_KEY.getBytes(), AES_STR);
            Cipher cipher = Cipher.getInstance(INSTANCE_STR);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(content);
            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * decrypting bytes
     *
     * @param content bytes before decryption
     * @return results bytes after decryption
     * @throws Exception
     */
    public static byte[] aesDecode(byte[] content) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(P_KEY.getBytes(), AES_STR);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(INSTANCE_STR);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypting string
     *
     * @param content string before encryption
     * @return encoded string
     */
    public static String aesEncryptString(String content) {
        if (StringUtils.isBlank(content)) {
            return StringUtils.EMPTY;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(P_KEY.getBytes(), AES_STR);
            Cipher cipher = Cipher.getInstance(INSTANCE_STR);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            byte[] encrypted = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Decrypting string
     *
     * @param content string before decryption
     * @return result  bytes after decryption
     * @throws Exception
     */
    public static String aesDecodeString(String content) {
        if (StringUtils.isBlank(content)) {
            return StringUtils.EMPTY;
        }
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(P_KEY.getBytes(), AES_STR);
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(INSTANCE_STR);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
            byte[] result = cipher.doFinal(Base64.decode(content));
            return new String(result, 0, result.length, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }

}
