package com.kk.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;

public class GsonTransform {

    public static <T> T parse(String data, Class<T> c) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        T ret = gson.fromJson(data, c);
        return ret;
    }


    public static <T> String transform(Object param, Class<T> c) {
//        Gson gson = new Gson();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        // 将简单bean转成
        String ret = gson.toJson(param, c);
        return ret;
    }
}
