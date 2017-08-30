package com.kk.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * @auther zhihui.kzh
 * @create 29/8/1711:54
 */
public class ByteBufTest {

    @Test
    public void testEmpty() {
        // 堆内存
        ByteBuf heapBuffer = Unpooled.buffer();
        System.out.println(heapBuffer.getClass().getSimpleName()); // InstrumentedUnpooledUnsafeHeapByteBuf

        // 直接内存
        ByteBuf direcBuffer = Unpooled.directBuffer();
        System.out.println(direcBuffer.getClass().getSimpleName()); // InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf
    }

    // 字符串，字节数组，ByteBuf 转换
    @Test
    public void testByte() {
        byte[] bytes = "abc".getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);

        System.out.println(byteBuf.getClass().getSimpleName()); // UnpooledHeapByteBuf


        // 字节长度
        System.out.println(byteBuf.capacity());

        bytes = byteBuf.array();
        System.out.println(new String(bytes));

    }


    // int，ByteBuf 转换
    @Test
    public void testInt() {
        {
            ByteBuf byteBuf = Unpooled.copyInt(9);

            System.out.println(byteBuf.capacity()); // 4

            System.out.println(byteBuf.readInt()); // 9
        }
        System.out.println("-----------------------------");
        {
            ByteBuf byteBuf = Unpooled.copyInt(9, 10, 11);

            System.out.println(byteBuf.capacity()); // 12

            System.out.println(byteBuf.getInt(0)); // 9
            System.out.println(byteBuf.getInt(4)); // 10
            System.out.println(byteBuf.getInt(8)); // 11
        }
        System.out.println("-----------------------------");
        {
            // 空 byteBuf，指定初始capacity。
            ByteBuf byteBuf = Unpooled.buffer(32);

            System.out.println(byteBuf.capacity()); // 32

            byteBuf.writeInt(100);
            byteBuf.writeInt(200);

            System.out.println(byteBuf.capacity()); // 32

            System.out.println(byteBuf.readInt()); // 100
            System.out.println(byteBuf.readInt()); // 200


        }
    }
}
