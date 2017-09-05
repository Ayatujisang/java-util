package com.kk.test;

import io.netty.buffer.*;
import io.netty.util.ByteProcessor;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * 写入数据到ByteBuf后，写入索引是增加的字节数量。开始读字节后，读取索引增加。
 * 你可以读取字节，直到写入索引和读取索引处理相同的位置，次数若继续读取，则会抛出 IndexOutOfBoundsException。
 * <p>
 * 调用 ByteBuf 的任何方法开始读/写都会单独维护读索引和 写索引。ByteBuf 的默认最大容量限制是 Integer.MAX_VALUE，写入时若超出这个值将会 导致一个异常。
 * <p>
 * ByteBuf 类似于一个字节数组，最大的区别是读和写的索引可以用来控制对缓冲区数 据的访问
 * <p>
 * <p>
 * ByteBuf.discardReadBytes()可以用来清空 ByteBuf 中已读取的数据，从而使 ByteBuf 有多余的空间容纳新的数据，
 * 但是 discardReadBytes()可能会涉及内存复制，因为它需要移 动 ByteBuf 中可读的字节到开始位置，这样的操作会影响性能，
 * 一般在需要马上释放内存的 时候使用收益会比较大。
 *
 * @auther zhihui.kzh
 * @create 29/8/1711:54
 */
public class ByteBufTest {

    @Test
    public void testEmpty() {
        {
            // 堆内存
            ByteBuf heapBuffer = Unpooled.buffer();
            System.out.println(heapBuffer.getClass().getSimpleName()); // InstrumentedUnpooledUnsafeHeapByteBuf

            // 直接内存
            ByteBuf direcBuffer = Unpooled.directBuffer();
            System.out.println(direcBuffer.getClass().getSimpleName()); // InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf

            //创建复合缓冲区
            CompositeByteBuf compBuf = Unpooled.compositeBuffer(); //创建堆缓冲区
            System.out.println(compBuf.getClass().getSimpleName()); // CompositeByteBuf

            compBuf.addComponents(heapBuffer, direcBuffer);
            //删除第一个ByteBuf
            compBuf.removeComponent(0);
        }

        // 内存池
        {
            // directByDefault参数为true，强制为直接内存
            ByteBuf heapBuffer = PooledByteBufAllocator.DEFAULT.buffer();

            System.out.println(heapBuffer.getClass().getSimpleName()); // PooledUnsafeDirectByteBuf

            // 直接内存
            ByteBuf direcBuffer = PooledByteBufAllocator.DEFAULT.directBuffer();
            System.out.println(direcBuffer.getClass().getSimpleName()); // PooledUnsafeDirectByteBuf
        }

    }

    // 字符串，字节数组，ByteBuf 转换
    @Test
    public void testByte() {
        byte[] bytes = "abc".getBytes();
        ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);

        System.out.println(byteBuf.getClass().getSimpleName()); // UnpooledHeapByteBuf

        // 返回指定 ByteBuf 中可读字节的十六进制字符串，可以用于调试程序时打印 ByteBuf 的内 容
        System.out.println(ByteBufUtil.hexDump(byteBuf)); // 616263

        // 遍历byteBuf，查询是否包含某字节，返回位置，不存在则返回-1。
        int i = byteBuf.forEachByte(new ByteProcessor.IndexOfProcessor((byte) 'c'));
        System.out.println(i); // 2

        // ByteBuf 转string，需要指定编码。
        System.out.println(byteBuf.toString(Charset.forName("utf-8"))); // abc

        // 字节长度
        System.out.println(byteBuf.capacity()); // 3

        // 获取字节数组
        bytes = byteBuf.array();

        System.out.println(new String(bytes)); // abc

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
