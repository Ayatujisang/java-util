package com.kk.utils;

import com.kk.utils.encode.SHA1Util;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ValidateUtil {
    private static Log logger = LogFactory.getLog(ValidateUtil.class);


    /**
     * 验证是否是手机号
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhone(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return false;
        }
        if (phoneNumber.length() != 11) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        if (!pattern.matcher(phoneNumber).matches()) {
            return false;
        }
        return true;
    }

    /**
     * 验证是否是邮箱
     *
     * @param mailAddr
     * @return
     */
    public static boolean isEmail(String mailAddr) {
        if (mailAddr == null) {
            return false;
        }

        if (mailAddr.length() < 1) {
            return true;
        }

        if (mailAddr.length() > 50) {
            return false;
        }

        boolean isValid = true;
        mailAddr = mailAddr.trim();

        // Check at-sign and white-space usage
        int atSign = mailAddr.indexOf('@');
        if (atSign == -1 || atSign == 0 || atSign == mailAddr.length() - 1
                || mailAddr.indexOf('@', atSign + 1) != -1
                || mailAddr.indexOf(' ') != -1 || mailAddr.indexOf('\t') != -1
                || mailAddr.indexOf('\n') != -1 || mailAddr.indexOf('\r') != -1) {
            isValid = false;
        }
        // Check dot usage
        if (isValid) {
            mailAddr = mailAddr.substring(atSign + 1);
            int dot = mailAddr.indexOf('.');
            if (dot == -1 || dot == 0 || dot == mailAddr.length() - 1) {
                isValid = false;
            }
        }
        return isValid;
    }

    // wi =2(n-1)(mod 11);加权因子

    final static int[] wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4,
            2, 1};

    // 校验码

    final static int[] vi = {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};

    private static int[] ai = new int[18];

    /**
     * 校验身份证
     *
     * @param idcard
     * @return
     */
    public static boolean isIdCard(String idcard) {
        idcard = idcard.toUpperCase();// 小写x 转成大写X
        if (idcard.length() == 15) {
            idcard = uptoeighteen(idcard);
        }
        if (idcard.length() != 18) {
            return false;
        }
        String verify = idcard.substring(17, 18);
        if (verify.equals(getVerify(idcard))) {
            return true;
        }
        return false;
    }

    // 15位转18位
    private static String uptoeighteen(String fifteen) {
        StringBuffer eighteen = new StringBuffer(fifteen);
        eighteen = eighteen.insert(6, "19");
        return eighteen.toString();
    }

    // 计算最后一位校验值
    private static String getVerify(String eighteen) {
        int remain = 0;
        if (eighteen.length() == 18) {
            eighteen = eighteen.substring(0, 17);
        }
        if (eighteen.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = eighteen.substring(i, i + 1);
                ai[i] = Integer.valueOf(k);
            }
            for (int i = 0; i < 17; i++) {
                sum += wi[i] * ai[i];
            }
            remain = sum % 11;
        }
        return remain == 2 ? "X" : String.valueOf(vi[remain]);

    }

    /**
     * 验证微信公众账号 验证微信开发者信息
     *
     * @param nonce
     * @param timestamp
     * @param signature
     * @return
     */
    public static boolean validateWX(long nonce, long timestamp,
                                     String signature, String token) {
        String sha1 = generateWXSignature(nonce, timestamp, token);
        return sha1.equalsIgnoreCase(signature);
    }

    /**
     * 生成微信signature
     *
     * @param nonce
     * @param timestamp
     * @param token
     * @return
     */
    public static String generateWXSignature(long nonce, long timestamp,
                                             String token) {
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(nonce));
        list.add(String.valueOf(timestamp));
        list.add(token);
        Collections.sort(list);
        String str = list.get(0) + list.get(1) + list.get(2);

        String sha1 = SHA1Util.sha1(str);
        return sha1;
    }
}
