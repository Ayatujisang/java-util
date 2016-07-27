package com.kk.utils.http;

import java.io.UnsupportedEncodingException;


public class URLEncoderUtil {
	
	public static String encodeUtf8(final String str){
		if(str == null){
			return str;
		}
		return encode(str, "UTF-8", null);
	}
	
	public static String encode(final String str, final String enc, final String def) {
		try {
			return java.net.URLEncoder.encode(str, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			return def;
		}
	}
	
	public static void main(final String[] args) {
		System.out.println(encodeUtf8("孔智慧"));
	}
}
