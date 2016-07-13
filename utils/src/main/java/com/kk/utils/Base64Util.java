package com.kk.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
    // 将 s 进行 BASE64 编码
    public static String encode(String s) {
        if (s == null) return null;
        return new String(Base64.encodeBase64(s.getBytes()));
    }

    // 将 BASE64 编码的字符串 s 进行解码
    public static String decode(String s) {
        if (s == null) return null;
        try {
            byte[] b = Base64.decodeBase64(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }

}
