package com.kk.test;

import io.netty.util.internal.SystemPropertyUtil;
import org.junit.Test;

/**
 * @auther zhihui.kzh
 * @create 29/8/1713:09
 */
public class SystemPropertyTest {

    @Test
    public void testSystem() {
        System.setProperty("com.kk.id", "1");

        System.out.println(SystemPropertyUtil.get("com.kk.id"));

        System.out.println(SystemPropertyUtil.getInt("com.kk.id", 0));
    }
}
