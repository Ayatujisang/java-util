package com.kk.utils.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * apache-httpclient
 * 设置UA:
 * getMethod.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
 */
public class HttpUtil {
    private static Log logger = LogFactory.getLog(HttpUtil.class);

    private static int DEFAULT_TIMEOUT = 6000;
    private static String encoding = "utf-8";
    private static String contentType = "application/json";


    public static String sendGet(String url) {

        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_TIMEOUT);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_TIMEOUT);

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(getMethod);
        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
        String content = null;
        if (statusCode == HttpStatus.SC_OK) {
            try {
//                byte[] responseBody = getMethod.getResponseBody();
//                content = new String(responseBody, encoding);

                InputStream in = getMethod.getResponseBodyAsStream();
                content = IOUtils.toString(in,encoding);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                // 释放连接
                getMethod.releaseConnection();
            }
        } else {
            logger.error("statusCode is not success(200):" + statusCode);
        }
        return content;
    }

    public static String sendPost(String url, Map<String, String> paramMap) {

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_TIMEOUT);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_TIMEOUT);

        // 填入各个表单域的值
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            NameValuePair nvp = new NameValuePair(entry.getKey(), entry.getValue());
            nvps.add(nvp);
        }
        NameValuePair[] data = nvps.toArray(new NameValuePair[0]);
        // 将表单的值放入postMethod中
        //可以传递post的data值
        postMethod.setRequestBody(data);

        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
        String content = null;
        if (statusCode == HttpStatus.SC_OK) {
            try {
//                byte[] responseBody = postMethod.getResponseBody();
//                content = new String(responseBody, encoding);

                InputStream in = postMethod.getResponseBodyAsStream();
                content = IOUtils.toString(in,encoding);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                // 释放连接
                postMethod.releaseConnection();
            }
        } else {
            logger.error("statusCode is not success(200):" + statusCode);
        }
        return content;
    }

    public static String sendPost(String url, String data) {

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);

        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_TIMEOUT);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_TIMEOUT);

        try {
            postMethod.setRequestEntity(new StringRequestEntity(data, contentType, encoding));
        } catch (Exception e) {
            e.printStackTrace();
        }


        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(postMethod);
        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
        String content = null;
        if (statusCode == HttpStatus.SC_OK) {
            try {
//                byte[] responseBody = postMethod.getResponseBody();
//                content = new String(responseBody, encoding);

                InputStream in = postMethod.getResponseBodyAsStream();
                content = IOUtils.toString(in,encoding);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                // 释放连接
                postMethod.releaseConnection();
            }
        } else {
            logger.error("statusCode is not success(200):" + statusCode);
        }
        return content;
    }
}