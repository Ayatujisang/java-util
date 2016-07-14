package com.kk.utils.json.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kk.utils.json.FastJsonUtil;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * 对date类型转json默认是转成时间戳
 * <p/>
 * 如果需要对json格式化， toJSONString加上SerializerFeature.PrettyFormat 即可。
 */
public class FastJsonTest {

    public static void main(String[] args) {
        /******************** 常规操作 ***************/
        UserInfo info = new UserInfo("zhangsan", 24);
        info.setDate(new Date());

        System.out.println(FastJsonUtil.toJsonString(info));
        System.out.println(FastJsonUtil.toJsonString(info, true));
        System.out.println(FastJsonUtil.toJsonStringWithFormatDate(info));
        System.out.println(FastJsonUtil.toJsonStringWithFormatDate(info, "yyyy-MM-dd"));

        net.sf.json.JSONObject xx = new net.sf.json.JSONObject();
        xx.put("A", 1);
        System.out.println(FastJsonUtil.toJsonString(xx));
        System.out.println(FastJsonUtil.toJsonString(xx, true));

        JSONObject yy = new JSONObject();
        yy.put("aa", 1);

        System.out.println(yy.getIntValue("aa"));
        System.out.println(FastJsonUtil.toJsonString(yy));
        System.out.println(FastJsonUtil.toJsonString(yy, true));

        // 将对象转换为JSON字符串
        String str_json = JSON.toJSONString(info);   // JsonObject j = JSON.toJSON(info);
        str_json = FastJsonUtil.toJsonStringWithFormatDate(info, "yyyy-MM-dd HH:mm:ss");
        // String str_json = JSON.toJSONString(info, true); // 格式化的json
        // String str_json = JSON.toJSONString(info,
        // SerializerFeature.WriteDateUseDateFormat); //带格式的日期
        System.out.println("JSON=" + str_json);

        // 将字符串转成 json对象
//        JSONObject obj = JSONObject.parseObject(str_json);
        JSONObject obj = FastJsonUtil.parseObject(str_json);
        System.out.println(obj.keySet() + ".." + obj.get("age"));

        // 将字符串转成对象
        info = JSON.parseObject(str_json, UserInfo.class);
        System.out.println(info);

        info = FastJsonUtil.parseObject(str_json, UserInfo.class);
        System.out.println(info);

        /******************** 数组操作 ***************/
        List<UserInfo> infos = new ArrayList<UserInfo>();
        infos.add(new UserInfo("zhangsan", 24));
        infos.add(new UserInfo("lisi", 25));

        // 将数组转成json字符串
        str_json = JSON.toJSONString(infos);
        System.out.println("JSON=" + str_json);
        str_json = FastJsonUtil.toJsonStringWithFormatDate(infos, "yyyy-MM-dd");
        System.out.println("JSON=" + str_json);

        // 将字符串转成 json数组
        JSONArray jar = JSONArray.parseArray(str_json);
        jar = FastJsonUtil.parseArray(str_json);
        System.out.println(jar + ".." + jar.get(0));

        // 将字符串转成数组
        infos = JSON.parseArray(str_json, UserInfo.class);
        infos = FastJsonUtil.parseArray(str_json, UserInfo.class);
        System.out.println(infos + "..." + infos.get(0).getDate().toLocaleString());
        // 将字符串转成数组 2
        infos = JSON.parseObject(str_json, new TypeReference<List<UserInfo>>() {
        });
        System.out.println(infos + "///" + infos.get(0).getDate().toLocaleString());

        /************** 将json字符串转化成List<Map<String,Object>>对象 **********/
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("key1", 1);
        map2.put("key2", 2);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        list.add(map2);
        // map数组转成字符串
        String jsonString = JSON.toJSONString(list);
        System.out.println("json字符串:" + jsonString);
        jsonString = FastJsonUtil.toJsonString(list);
        System.out.println("json字符串:" + jsonString);
        // 解析json字符串
        List<Map<String, Object>> list2 = JSON.parseObject(jsonString,
                new TypeReference<List<Map<String, Object>>>() {
                });
        System.out.println(list2);

        /*********** 日期操作 ***********/

        Date date = new Date();
        // 输出毫秒值
        System.out.println(JSON.toJSONString(date));
        // 默认格式为yyyy-MM-dd HH:mm:ss
        System.out.println(JSON.toJSONString(date,
                SerializerFeature.WriteDateUseDateFormat));
        // 根据自定义格式输出日期
        System.out.println(JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat));

        System.out.println(FastJsonUtil.parseObjectFromBean(info));

    }
}