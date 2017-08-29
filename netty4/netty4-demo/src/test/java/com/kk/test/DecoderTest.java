package com.kk.test;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @auther zhihui.kzh
 * @create 29/8/1713:02
 */
public class DecoderTest {

    @Test
    public void test_DefaultCharset() {
        System.out.println("默认编码：" + Charset.defaultCharset());
    }
}
