package com.kk.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {

    public static String BAIDU_SHORT_SERVICE = "http://dwz.cn/create.php";

    public static String SINA_SHORT_SERVICE = "http://api.t.sina.com.cn/short_url/shorten.json?source=1681459862&url_long=";

    // 获取url中 参数的值
    public static String getParameter(String url, String parameter) {
        if (url == null) {
            return null;
        }
        String reg = "(^|&|\\?)" + parameter + "=([^&]*)(&|$)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            try {
                String src = URLDecoder.decode(matcher.group(2), "UTF-8");
                return src;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 将参数Map转化为Url的字符串形式
     *
     * @param paramMap key为字符串，value为字符串或字符串数组
     * @return
     */
    public static <T> String toParamString(Map<String, T> paramMap) {
        if (MapUtils.isEmpty(paramMap)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, T> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            T value = entry.getValue();
            if (key == null) {
                continue;
            }
            if (value == null || !value.getClass().isArray()) {
                sb.append("&").append(URLEncoderUtil.encodeUtf8(key)).append("=")
                        .append(URLEncoderUtil.encodeUtf8(ObjectUtils.toString(value)));
            } else if (value instanceof Object[]) {
                for (Object object : (Object[]) value) {
                    sb.append("&").append(URLEncoderUtil.encodeUtf8(key)).append("=")
                            .append(URLEncoderUtil.encodeUtf8(ObjectUtils.toString(object)));
                }
            }
        }
        if (sb.length() == 0) {
            return "";
        }
        return sb.substring(1);
    }

    /**
     * 生成短连接
     */
    public static String generateShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        String shortUrl = generateBaiduShortUrl(url);
        if (shortUrl.equals(url)) {
            shortUrl = generateSinaShortUrl(url);
        }
        return shortUrl;
    }

    // 新浪 短链接
    public static String generateSinaShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        try {
            String jsonStr = HttpClientUtil.sendGet(SINA_SHORT_SERVICE + url);

            if (StringUtils.isBlank(jsonStr)) {
                return url;
            }
            JSONArray jsonArray = JSON.parseArray(jsonStr);
            if (jsonArray.size() == 1) {
                return jsonArray.getJSONObject(0).getString("url_short");
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    // 百度 短链接
    public static String generateBaiduShortUrl(String url) {
        if (StringUtils.isBlank(url)) {
            return "";
        }
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("url", url);

            String jsonStr = HttpClientUtil.sendPost(BAIDU_SHORT_SERVICE, params, "utf-8");
            JSONObject object = JSON.parseObject(jsonStr);

            String shortUrl = null;
            if (object.getString("status").equals("0")) {
                shortUrl = object.getString("tinyurl");
            } else {
                shortUrl = url;
            }
            return shortUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void main(String[] args) {
//        System.out.println(getParameter("page=2&t=1", "page"));
//        System.out.println(generateShortUrl("https://www.baidu.com"));

        Map map = new HashMap();
        map.put("key null", null);
        map.put("key1", "value1");
        map.put("key space", "value2");
        map.put("key string array", new String[]{"value31", "value32", "value33"});
        map.put("key int array", new int[]{1, 2, 3});
        System.out.println(toParamString(map));

    }

}