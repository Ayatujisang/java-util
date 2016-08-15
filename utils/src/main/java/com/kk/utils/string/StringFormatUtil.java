package com.kk.utils.string;

import java.text.MessageFormat;

public class StringFormatUtil {

    // msg如 %s,%d等
    public static String format(String msg, Object... args) {
        return String.format(msg, args);
    }

    /**
     *
     FormatElement:
         { ArgumentIndex }
         { ArgumentIndex , FormatType }
         { ArgumentIndex , FormatType , FormatStyle }

     FormatType:
         number
         date
         time
         choice（需要使用ChoiceFormat）

     FormatStyle:
         short
         medium
         long
         full
         integer
         currency
         percent
         SubformatPattern（子模式）

     还以str为例，在这个字符串中：
     1、{0}和{1,number,short}和{2,number,#.#};都属于FormatElement，0,1,2是ArgumentIndex。
     2、{1,number,short}里面的number属于FormatType，short则属于FormatStyle。
     3、{1,number,#.#}里面的#.#就属于子格式模式。

     * @param msg
     * @param args
     * @return
     */
    // msg如 {0}， {}左右不能加单引号，否则无法转义
    // 格式化字符串时，两个单引号才表示一个单引号，单个单引号会被省略
    // 无论是有引号字符串还是无引号字符串，左花括号都是不支持的，但支持右花括号显
    public static String format2(String msg, Object... args) {
        return MessageFormat.format(msg, args);
    }

    public static void main(String[] args) {
        System.out.println(format("%s is 'd' %d", "a", 1)); // a is 'd' 1
        System.out.println(format2("{0} is ''d'' {1}", "a", 1)); // a is 'd' 1
        System.out.println(format2("'{0}' is ''d'' {1}", "a", 1)); // {0} is 'd' 1
        System.out.println(format2("'{0}' is ''d''  {1,number,#.#}", "a", 1)); // {0} is 'd'  1
        System.out.println(format2("'{0}' is ''d''  {1,number,#.0}", "a", 1)); // {0} is 'd'  1.0
    }
}
