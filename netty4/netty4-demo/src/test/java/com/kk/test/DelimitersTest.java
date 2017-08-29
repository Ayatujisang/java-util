package com.kk.test;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import org.junit.Test;

/**
 * 分隔符
 *
 * @auther zhihui.kzh
 * @create 29/8/1711:51
 */
public class DelimitersTest {

    @Test
    public void test_line() {
        // \r\n ,\n
        ByteBuf[] ds = Delimiters.lineDelimiter();
    }

    @Test
    public void test_null() {
        // \0
        ByteBuf[] ds = Delimiters.nulDelimiter();
    }

}
