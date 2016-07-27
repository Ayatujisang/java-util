package com.kk.utils.http;

import java.io.UnsupportedEncodingException;

public class URLDecoderUtil {

    public static String decodeUtf8(final String str) {
        if (str == null) {
            return str;
        }
        return decode(str, "UTF-8", null);
    }

    public static String decode(final String str, final String enc, final String def) {
        try {
            return java.net.URLDecoder.decode(str, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return def;
        }
    }

    public static void main(String[] args) {
        System.out.println(decodeUtf8("%E5%AD%94%E6%99%BA%E6%85%A7"));
    }
}

