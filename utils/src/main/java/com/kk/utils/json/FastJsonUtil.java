package com.kk.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对date类型转json默认是转成时间戳
 * <p/>
 * 如果需要对json格式化， toJSONString加上SerializerFeature.PrettyFormat 即可。
 * <p/>
 * jsonObject中 key升序排列
 * <p/>
 * 转json时候看的是 set，get方法
 * <p/>
 * JSONObject  获取值，调用  get 值不存在时候返回null
 * <p/>
 * getInteger 返回Integer,不存在时候返回null；  getIntValue 返回int，不存在时候 返回默认值0.
 */

public class FastJsonUtil {
    // obj 普通bean,net.sf.jsonObject,fastjson,list,map
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * obj 普通bean,net.sf.jsonObject,fastjson
     * <p/>
     * 对json 格式化
     */
    public static String toJsonString(Object obj, boolean pretty) {
        return JSON.toJSONString(obj, pretty);
    }

    // obj 普通bean 对日期进行格式化，默认格式为yyyy-MM-dd HH:mm:ss
    // 如果想再进行格式化，则加上SerializerFeature.PrettyFormat即可。
    public static String toJsonStringWithFormatDate(Object obj) {
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat);
    }

    // obj 普通bean 对日期进行格式化 指定格式化方式, formt例如： yyyy-MM-dd
    public static String toJsonStringWithFormatDate(Object obj, String format) {
        return JSON.toJSONStringWithDateFormat(obj, format, SerializerFeature.WriteDateUseDateFormat);
    }

    // 字符串转 json
    public static JSONObject parseObject(String str) {
        JSONObject obj = JSONObject.parseObject(str);
        return obj;
    }

    // 对象转json
    public static JSONObject parseObjectFromBean(Object bean) {
        JSONObject obj = JSONObject.parseObject(toJsonString(bean));
        return obj;
    }

    // 字符串转jsonarray
    public static JSONArray parseArray(String str) {
        JSONArray jar = JSONArray.parseArray(str);
        return jar;
    }

    // 反解析时候，date类型 支持时间戳，yyy-MM-dd HH:mm:ss，yyyy-MM-dd
    public static <T> T parseObject(String str, Class<T> c) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return JSON.parseObject(str, c);
    }

    // 反解析，date类型 支持时间戳，yyy-MM-dd HH:mm:ss，yyyy-MM-dd
    public static <T> List<T> parseArray(String str, Class<T> c) {
        if (StringUtils.isBlank(str)) {
            return new ArrayList<T>();
        }
        return JSON.parseArray(str, c);
    }
    // 反解析
    // List<Map<String, Object>> list2 = JSON.parseObject(jsonString,
//    new TypeReference<List<Map<String, Object>>>() {
//    });
}
