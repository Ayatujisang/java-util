package com.kk.utils.json.test;

import com.kk.utils.date.DateUtil;
import com.kk.utils.json.JsonDateValueProcessor;
import com.kk.utils.json.SfJsonUtil;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * net.sf.json  parse  map2json时候 key不能保证升序排列。
 * <p/>
 * 要求class是 public类型，
 */
public class SfJsonTest {

    private static JSONArray jsonArray = null;
    private static JSONObject jsonObject = null;


    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {

        jsonArray = new JSONArray();
        jsonObject = new JSONObject();

        UserInfo bean = new UserInfo("kkk", 22);
        bean.setDate(DateUtil.getDate(2016, 1, 1, 2, 2, 2));
        System.out.println(bean);


        String xxx = SfJsonUtil.toJsonObjectWithDateFormat(bean).toString();
        System.out.println("xxx=" + xxx);

        System.out.println("yy3=" + SfJsonUtil.parseObjectWithDateFormat(xxx, UserInfo.class));

        // 将类转换为 json的string类型
        System.out.println(JSONObject.fromObject(bean).toString());

        // 将类转换为 jsonarray的string类型
        System.out.println(JSONArray.fromObject(bean).toString());
        System.out.println(SfJsonUtil.toJsonObject(bean).toString());
        // 将类转换为json类型
        jsonObject = JSONObject.fromObject(bean);
        System.out.println("jsonObject=" + jsonObject);

//        // 将json转换为类
        UserInfo student = (UserInfo) JSONObject
                .toBean(jsonObject, UserInfo.class);
        System.out.println(student);
        student = SfJsonUtil.parseObject(jsonObject, UserInfo.class);
        System.out.println(student);
//
//        // 从json中得到值
        System.out.println(jsonObject.get("name") + ".." + jsonObject.opt("name"));
        System.out.println(jsonObject.get("birthday"));
        System.out.println(jsonObject.optInt("id"));
//
//        // json array
        List<UserInfo> stu = new ArrayList<UserInfo>();
        stu.add(student);
        stu.add(bean);
        // 将list转换为json array 的string
        System.out.println(JSONArray.fromObject(stu).toString());
        System.out.println(SfJsonUtil.toJsonArray(stu));
        System.out.println("dte.jar=" + SfJsonUtil.toJsonArrayWithDateFormat(stu));
//        // 将json array 转换为list类型
        jsonArray = JSONArray.fromObject(stu);
        List<UserInfo> li = JSONArray.toList(jsonArray, UserInfo.class);
        System.out.println(li);
        li = SfJsonUtil.parseList(jsonArray, UserInfo.class);
        System.out.println(li);
//
//        // 将map转换成json
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("A", bean);

        bean.setName("jack");
        map.put("B", bean);
        map.put("name", "json");
        map.put("bool", Boolean.TRUE);
        map.put("int", new Integer(1));
        map.put("arr", new String[]{"a", "b"});
        map.put("func", "function(i){ return this.arr[i]; }");
        // 将map转传成json的string
        System.out.println(JSONObject.fromObject(map).toString());
        System.out.println(SfJsonUtil.toJsonObject(map));
//        // 将map转传成json array的string
//        System.out.println(JSONArray.fromObject(map).toString());
//
//        // json字符串转换为类
//        String jsonstring = "{\"address\":\"chian\",\"birthday\":{\"birthday\":\"2010-11-22\"},"
//                + "\"email\":\"email@123.com\",\"id\":22,\"name\":\"tom\"}";
//        jsonObject = JSONObject.fromObject(jsonstring);
//        UserInfo stunew = (UserInfo) JSONObject.toBean(jsonObject, UserInfo.class);
//        System.out.println(stunew);
//
//        // 将json字符串转成Java的Array数组
//        jsonstring = "[" + jsonstring + "]";
//        jsonArray = JSONArray.fromObject(jsonstring);
//        System.out.println(jsonArray.get(0).toString());
//        Object[] os = jsonArray.toArray();
//        System.out.println(os.length);
//
//        System.out.println(JSONArray.fromObject(jsonstring).join(""));
//        System.out.println(os[0].toString());
        UserInfo[] stus = (UserInfo[]) JSONArray
                .toArray(jsonArray, UserInfo.class);
        System.out.println(stus.length);
        System.out.println(stus[0]);

        stus = SfJsonUtil.toArray(jsonArray, UserInfo.class);
        System.out.println(stus.length);
        System.out.println(stus[0]);
//
//        // 将json字符串转成Java的Array数组
//        jsonArray = JSONArray.fromObject(jsonstring);
//        List<UserInfo> list = JSONArray.toList(jsonArray, UserInfo.class);
//        System.out.println(list.size());
//        System.out.println(list.get(0));
//
//        list = JSONArray.toList(jsonArray);
//        System.out.println(list.size());
//        System.out.println(list.get(0));
//
//        // 将json字符串转换成map集合
//        jsonstring = "{\"arr\":[\"a\",\"b\"],\"A\":{\"address\":\"address\",\"birthday\":{\"birthday\":\"2010-11-22\"},"
//                + "\"email\":\"email\",\"id\":1,\"name\":\"jack\"},\"int\":1,"
//                + "\"B\":{\"address\":\"address\",\"birthday\":{\"birthday\":\"2010-11-22\"},"
//                + "\"email\":\"email\",\"id\":1,\"name\":\"jack\"},\"name\":\"json\",\"bool\":true}";
//        jsonObject = JSONObject.fromObject(jsonstring);
//        Map<String, Class<?>> clazzMap = new HashMap<String, Class<?>>();
//        clazzMap.put("arr", String[].class);
//        clazzMap.put("A", UserInfo.class);
//        clazzMap.put("B", UserInfo.class);
//        Map<String, ?> mapBean = (Map) JSONObject.toBean(jsonObject, Map.class,
//                clazzMap);
//        System.out.println(mapBean);
//
//        Set<String> set = mapBean.keySet();
//        Iterator<String> iter = set.iterator();
//        while (iter.hasNext()) {
//            String key = iter.next();
//            System.out.println(key + ":" + mapBean.get(key).toString());
//        }

    }
}
