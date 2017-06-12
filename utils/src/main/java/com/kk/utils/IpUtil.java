package com.kk.utils;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {

    static int IP_ARRAY_LENGTH = 4;

    /**
     * number类型的ip转换为string类型
     *
     * @param ip 2130706433
     * @return String xxx.xxx.xxx.xxx
     */
    public static String decodeIp(long ip) {
        if (ip <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append((ip >> 24) & 0xFF).append(".")
                .append((ip >> 16) & 0xFF).append(".")
                .append((ip >> 8) & 0xFF).append(".")
                .append(ip & 0xFF);
        return sb.toString();
    }

    /**
     * string类型的ip转换为number类型
     *
     * @param ipString xxx.xxx.xxx.xxx
     * @return long 2130706433
     */
    public static long encodeIp(String ipString) {
        long ipNumber = 0;
        if (StringUtils.isNotBlank(ipString)) {
            String[] ipArray = StringUtils.split(ipString, ".");
            if (ipArray.length == IP_ARRAY_LENGTH) {
                ipNumber = (Long.parseLong(ipArray[0]) << 24)
                        | (Long.parseLong(ipArray[1]) << 16)
                        | (Long.parseLong(ipArray[2]) << 8)
                        | (Long.parseLong(ipArray[3]));
            }
        }
        return ipNumber;
    }

    /**
     * 获取本地Ip
     *
     * @return
     */
    public static String getLocalAddr() {
        Enumeration interfaces = null;

        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException var4) {
            var4.printStackTrace();
            return null;
        }

        while (interfaces.hasMoreElements()) {
            NetworkInterface ifc = (NetworkInterface) interfaces.nextElement();
            Enumeration addressesOfAnInterface = ifc.getInetAddresses();

            while (addressesOfAnInterface.hasMoreElements()) {
                InetAddress address = (InetAddress) addressesOfAnInterface.nextElement();
                if (address.isSiteLocalAddress()) {
                    return address.getHostAddress();
                }
            }
        }

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(encodeIp("127.0.0.1"));
        System.out.println(decodeIp(2130706433));
        System.out.println(getLocalAddr());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
