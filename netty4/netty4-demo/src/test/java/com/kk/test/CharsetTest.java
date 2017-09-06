package com.kk.test;

import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * @auther zhihui.kzh
 * @create 29/8/1713:02
 */
public class CharsetTest {

    @Test
    public void test_DefaultCharset() {
        System.out.println("默认编码：" + Charset.defaultCharset());
    }

    @Test
    public void test_Charset() {
        System.out.println("utf-8编码：" + CharsetUtil.UTF_8);
        System.out.println("utf-8编码：" + Charset.forName("UTF-8"));
    }
}
