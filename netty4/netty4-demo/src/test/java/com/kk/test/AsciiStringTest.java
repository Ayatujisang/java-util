package com.kk.test;

import io.netty.util.AsciiString;
import org.junit.Test;

/**
 * 只支持ascii码的字符串，不支持中文
 *
 * @auther zhihui.kzh
 * @create 5/9/1720:41
 */
public class AsciiStringTest {

    @Test
    public void test() {
        AsciiString str = new AsciiString("abc");

        System.out.println(str.toString()); // abc

        System.out.println(str.subSequence(0, 2)); // ab
    }
}
