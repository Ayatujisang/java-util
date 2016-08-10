package com.kk.utils.string;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberFormatUtil {

    public static String transDouble(double price) {
        DecimalFormat df = new DecimalFormat("0.00"); // 保留2位小数
        return df.format(price);
    }

    // 0.10 ==> 0.1
    public static String convert(BigDecimal number) {
        if (number == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###.##");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }

    // 保留小数点后2位
    public static String convertTwo(BigDecimal number) {
        if (number == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }

    public static String convertInt(BigDecimal number) {
        if (number == null) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }

    public static String convertInt(int number) {
        if (number == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#,###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(number);
    }
}
