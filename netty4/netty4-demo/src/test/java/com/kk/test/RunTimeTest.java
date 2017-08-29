package com.kk.test;

import io.netty.util.NettyRuntime;
import org.junit.Test;

/**
 * @auther zhihui.kzh
 * @create 28/8/1717:11
 */
public class RunTimeTest {

    /**
     * 获取cpu数量
     */
    @Test
    public void test() {
        System.out.println(Runtime.getRuntime().availableProcessors());

        System.out.println(NettyRuntime.availableProcessors());
    }
}
