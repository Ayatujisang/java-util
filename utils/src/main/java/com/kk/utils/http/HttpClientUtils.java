package com.kk.utils.http;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *  apache-httpclient
 *  ¿Í»§¶Ë³Ø
 */
public class HttpClientUtils {
    private static final Log LOG = LogFactory.getLog(HttpClientUtils.class);

    public static final int DEFAULT_SO_TIMEOUT = 6000;                                          // 5s

    private static String encoding = "utf-8";
    private static String contentType = "application/json";

    private static final HttpClient CLIENT;

    static {
        MultiThreadedHttpConnectionManager connMgr = new MultiThreadedHttpConnectionManager();
        // set connection params
        HttpConnectionManagerParams params = connMgr.getParams();
        params.setConnectionTimeout(1000); // 1s
        params.setSoTimeout(DEFAULT_SO_TIMEOUT);
        params.setDefaultMaxConnectionsPerHost(20);
        params.setMaxTotalConnections(100);

        CLIENT = new HttpClient(connMgr);
    }

    public static HttpResponse doPost(String url, Map<String, String> params) {
        PostMethod method = new PostMethod(url);
        if (params != null && params.size() > 0) {
            method.addParameters(buildPostData(params));
        }

        return executeMethod(method, DEFAULT_SO_TIMEOUT);
    }


    public static HttpResponse doPost(String url, String data) {
        PostMethod method = new PostMethod(url);
        try {
            method.setRequestEntity(new StringRequestEntity(data, contentType, encoding));
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getMessage(), e);
        }
        return executeMethod(method, DEFAULT_SO_TIMEOUT);
    }


    public static HttpResponse doGet(String url) {
        GetMethod method = new GetMethod(url);
        return executeMethod(method, DEFAULT_SO_TIMEOUT);
    }


    private static HttpResponse executeMethod(HttpMethod method, int soTimeout) {
        HttpResponse result = new HttpResponse();

        method.getParams().setContentCharset(encoding);
        method.getParams().setSoTimeout(soTimeout);
        try {
            logStart(method);

            int statusCode = CLIENT.executeMethod(method);
            result.setCode(statusCode);

            String data = readString(method.getResponseBodyAsStream());
            result.setData(data);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrMsg(e.getMessage());
            logError(method, e);
        } finally {
            method.releaseConnection();
        }
        return result;
    }

    private static void logStart(HttpMethod method) throws Exception {
        if (LOG.isInfoEnabled()) {
            if (method instanceof PostMethod) {
                PostMethod postMethod = (PostMethod) method;
                LOG.info("POST: start to call url " + postMethod.getURI().toString() + " with params: "
                        + toString(postMethod.getParameters()));
            } else {
                LOG.info("GET: start to call url " + method.getURI().toString());
            }
        }
    }

    private static void logError(HttpMethod method, Exception e) {
        try {
            if (method instanceof PostMethod) {
                PostMethod postMethod = (PostMethod) method;
                LOG.error("POST: fail to call url " + postMethod.getURI().toString() + " with params: "
                        + toString(postMethod.getParameters()) + ", errorMsg: " + e.getMessage());
            } else {
                LOG.error("GET: fail to call url " + method.getURI().toString() + ", errorMsg: " + e.getMessage());
            }
        } catch (URIException e1) {
            // ignore
        }
    }

    private static String toString(NameValuePair[] pairs) {
        if (ArrayUtils.isEmpty(pairs)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (NameValuePair pair : pairs) {
            sb.append(pair.getName()).append("=").append(pair.getValue()).append(", ");
        }
        int index = sb.lastIndexOf(",");
        sb.delete(index, sb.length());
        sb.append("}");

        return sb.toString();
    }

    public static NameValuePair[] buildPostData(Map<String, String> params) {
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Entry<String, String> param : params.entrySet()) {
            NameValuePair pair = new NameValuePair();
            pair.setName(param.getKey());
            pair.setValue(param.getValue());

            pairs.add(pair);
        }

        NameValuePair[] result = new NameValuePair[params.size()];
        return pairs.toArray(result);
    }

    private static String readString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
        StrBuilder sb = new StrBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.appendln(line);
        }
        return sb.toString();
    }

    public static class HttpResponse {
        private int code = HttpStatus.SC_OK;
        private boolean success = true;
        private String errMsg;
        private String data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "HttpResponse [code=" + code + ", success=" + success + ", errMsg=" + errMsg + ", data=" + data
                    + "]";
        }
    }
}
