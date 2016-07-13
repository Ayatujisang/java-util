package com.kk.utils.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 获取web访问者 IP。
 * <p/>
 * 调用实例:String ip =RemoteAddrFetherImpl.getRemoteAddr(request);
 *
 * @author zhihui.kong
 */
public class RemoteAddrFetherImpl {

    /**
     * 获取ClientIp的Log
     */
    private final static Log logger = LogFactory
            .getLog(RemoteAddrFetherImpl.class);

    /**
     * 取客户端的真实IP，考虑了反向代理等因素的干扰
     *
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        if (logger.isDebugEnabled()) {// 打印所有日志
            logger.debug(new StringBuffer().append("X-Forwarded-For:")
                    .append(request.getHeader("X-Forwarded-For"))
                    .append("\tProxy-Client-IP:")
                    .append(request.getHeader("Proxy-Client-IP"))
                    .append("\t:WL-Proxy-Client-IP:")
                    .append(request.getHeader("WL-Proxy-Client-IP"))
                    .append("\tRemoteAddr:").append(request.getRemoteAddr())
                    .toString());
        }
        String ip;
        @SuppressWarnings("unchecked")
        Enumeration<String> xffs = request.getHeaders("X-Forwarded-For");
        if (xffs.hasMoreElements()) {
            String xff = xffs.nextElement();
            ip = resolveClientIPFromXFF(xff);
            if (isValidIP(ip)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("X-Forwarded-For" + ip);
                }
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isValidIP(ip)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Proxy-Client-IP" + ip);
            }
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isValidIP(ip)) {
            if (logger.isDebugEnabled()) {
                logger.debug("WL-Proxy-Client-IP" + ip);
            }
            return ip;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("None-Proxy-Client-IP" + ip);
        }
        return request.getRemoteAddr();
    }

    /**
     * 从X-Forwarded-For头部中获取客户端的真实IP。 X-Forwarded-For并不是RFC定义的标准HTTP请求Header
     * ，可以参考http://en.wikipedia.org/wiki/X-Forwarded-For
     *
     * @param xff X-Forwarded-For头部的值
     * @return 如果能够解析到client IP，则返回表示该IP的字符串，否则返回null
     */
    private static String resolveClientIPFromXFF(String xff) {
        if (xff == null || xff.length() == 0) {
            return null;
        }
        String[] ss = xff.split(",");
        for (int i = ss.length - 1; i >= 0; i--) {// x-forward-for链反向遍历
            String ip = ss[i].trim();
            if (isValidIP2(ip)) { // 判断ip是否合法
                return ip;
            }
        }

        // 如果反向遍历没有找到格式正确的外网IP，那就正向遍历找到第一个格式合法的IP
        for (int i = 0; i < ss.length; i++) {
            String ip = ss[i].trim();
            if (isValidIP2(ip)) {
                return ip;
            }
        }
        return null;
    }

    private static final Pattern ipPattern = Pattern
            .compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");

    private static boolean isValidIP(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return ipPattern.matcher(ip).matches();
    }

    // 去除内网IP
    private static boolean isValidIP2(String ip) {
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        if (ip.startsWith("10.") || ip.startsWith("192.168.")
                || ip.startsWith("100.") || ip.startsWith("101.200.106.")) {
            return false;
        }

        return ipPattern.matcher(ip).matches();
    }

}
