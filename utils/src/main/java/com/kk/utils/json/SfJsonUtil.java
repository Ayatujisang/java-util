package com.kk.utils.json;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import java.util.Date;
import java.util.List;

/**
 * net.sf.json  parse  map2json时候 key不能保证升序排列。
 * <p/>
 * 转json时候 看的是 set和get方法，
 * <p/>
 * 要求class是 public类型，
 * <p/>
 * date类型 默认转成： "date":{"date":14,"day":4,"hours":13,"minutes":17,"month":6,"seconds":48,"time":1468473468912,"timezoneOffset":-480,"year":116}
 * <p/>
 * JSONObject 获取值时候 调用get方法，不存在时候返回null。
 * <p/>
 * getString方法 返回string类型，key不存在时候 抛异常， 可以使用optString，返回默认值null。
 * getInt同上， 推荐使用optInt， key不存在时候 返回默认值0.
 */
public class SfJsonUtil {

    // 普通bean,map
    public static JSONObject toJsonObject(Object bean) {
        if (bean == null) {
            return null;
        }
        return JSONObject.fromObject(bean);
    }

    /**
     *   JsonConfig jsonConfig = new JsonConfig();
         jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
         JSONObject json = new JSONObject();
         json.putAll(map, jsonConfig);
     * @param bean
     * @return
     */
    // 普通bean,map，  date:yyyy-MM-dd HH:mm:ss
    public static JSONObject toJsonObjectWithDateFormat(Object bean) {
        if (bean == null) {
            return null;
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        return JSONObject.fromObject(bean, jsonConfig);
    }

    // 普通bean,list
    public static JSONArray toJsonArray(Object bean) {
        if (bean == null) {
            return null;
        }
        return JSONArray.fromObject(bean);
    }

    // 普通bean,list，date:yyyy-MM-dd HH:mm:ss
    public static JSONArray toJsonArrayWithDateFormat(Object bean) {
        if (bean == null) {
            return null;
        }
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
        return JSONArray.fromObject(bean, jsonConfig);
    }

    // json 转bean，date类型需要时toJsonObject时候的格式， 不支持 yyyy-MM-dd HH:mm:ss 格式
    public static <T> T parseObject(JSONObject obj, Class<T> c) {
        T t = (T) JSONObject.toBean(obj, c);
        return t;
    }

    // json 转bean，date支持 yyyy-MM-dd HH:mm:ss 格式
    public static <T> T parseObjectWithDateFormat(String JsonString, Class<T> clazz) {
        JSONUtils.getMorpherRegistry().registerMorpher(
                new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss"}));
        JSONObject jsonObject = JSONObject.fromObject(JsonString);
        T entity = (T) JSONObject.toBean(jsonObject, clazz);
        return entity;
    }

    // jar 转list
    public static <T> List<T> parseList(JSONArray jar, Class<T> c) {
        return JSONArray.toList(jar, c);
    }

    // jar 转 array
    public static <T> T[] toArray(JSONArray jar, Class<T> c) {
        return (T[]) JSONArray.toArray(jar, c);
    }
}
