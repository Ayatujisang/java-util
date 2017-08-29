package com.kk.test;

import io.netty.channel.ChannelOption;
import org.junit.Test;

/**
 * @auther zhihui.kzh
 * @create 29/8/1711:41
 */
public class ChannelOptionTest {

    @Test
    public void test(){
        ChannelOption<Integer> option = ChannelOption.SO_BACKLOG;
        System.out.println(option.id());
        System.out.println(option.name());
    }
}
