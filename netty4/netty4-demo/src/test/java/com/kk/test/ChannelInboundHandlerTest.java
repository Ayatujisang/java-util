package com.kk.test;

import com.kk.netty4.helloworld.server.HelloServerHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.TypeParameterMatcher;
import org.junit.Test;

/**
 * SimpleChannelInboundHandler 测试类
 *
 * @auther zhihui.kzh
 * @create 29/8/1714:30
 */
public class ChannelInboundHandlerTest {

    /**
     * 自定义handler类设置泛型，acceptInboundMessage方法检测参数是否和泛型类型一致。
     *
     * @throws Exception
     */
    @Test
    public void test_HelloServerHandler_String() throws Exception {
        HelloServerHandler handler = new HelloServerHandler();

        System.out.println(handler.acceptInboundMessage("s")); // true
        System.out.println(handler.acceptInboundMessage(1)); // false
        System.out.println(handler.acceptInboundMessage(true)); // false
    }

    @Test
    public void test_Genericity_String()  {
        SubClass handler = new SubClass();

        System.out.println(handler.accept("s")); // true
        System.out.println(handler.accept(1)); // false
        System.out.println(handler.accept(true)); // false
    }

    // StringUtil.simpleClassName 支持匿名内部类
    @Test
    public void test_simplaclass(){

        System.out.println(StringUtil.simpleClassName(this.getClass())); // ChannelInboundHandlerTest
        System.out.println(this.getClass().getSimpleName());  // ChannelInboundHandlerTest

        System.out.println(StringUtil.simpleClassName(new Runnable() {
            @Override
            public void run() {

            }
        })); // ChannelInboundHandlerTest$1

        System.out.println(new Runnable() {
            @Override
            public void run() {

            }
        }.getClass().getSimpleName()); // ""  ，空串
    }

}

// 自定义类，支持泛型
class Genericity<I> {

    private TypeParameterMatcher matcher;

    public Genericity() {
        matcher = TypeParameterMatcher.find(this, Genericity.class, "I");
    }

    public boolean accept(Object msg) {
        return matcher.match(msg);
    }
}

// 继承父类，设置泛型
class SubClass extends Genericity<String> {

}
