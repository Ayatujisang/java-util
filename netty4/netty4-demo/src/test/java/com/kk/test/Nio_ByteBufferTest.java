package com.kk.test;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
   内核态：控制计算机的硬件资源，并提供上层应用程序运行的环境。比如socket I/0操作或者文件的读写操作等
   用户态：上层应用程序的活动空间，应用程序的执行必须依托于内核提供的资源。
   系统调用：为了使上层应用能够访问到这些资源，内核为上层应用提供访问的接口。
 */


/**
   direct ByteBuffer可以通过-XX:MaxDirectMemorySize来设置，此参数的含义是当Direct ByteBuffer分配的堆外内存到达指定大小后，即触发Full GC。
   注意该值是有上限的，默认是64M，最大为sun.misc.VM.maxDirectMemory()，在程序中中可以获得-XX:MaxDirectMemorySize的设置的值
 */

/**
 * （1） 平时的read/write，都会在I/O设备与应用程序空间之间经历一个“内核缓冲区”。
 * （2） Direct Buffer就好比是“内核缓冲区”上的缓存，不直接受GC管理；
 * 而Heap Buffer就仅仅是byte[]字节数组的包装形式。因此把一个Direct Buffer写入一个Channel的速度要比把一个Heap Buffer写入一个Channel的速度要快。
 * （3） Direct Buffer创建和销毁的代价很高，所以要用在尽可能重用的地方。
 * <p>
 * <p>
 * 如果我们构造一个ByteBuffer仅仅使用一次，不复用它，那么Direct Buffer和Heap Buffer没有明显的区别。两个地方我们可能通过Direct Buffer来提高性能：
 * 1、 大文件，尽管我们Direct Buffer只用一次，但是如果内容很大，Heap Buffer的复制代价会很高，此时用Direct Buffer能提高性能。
 * 这就是为什么，当我们下载一个大文件时，服务端除了用SendFile机制，也可以用“内存映射”，把大文件映射到内存，也就是MappedByteBuffer，
 * 是一种Direct Buffer，然后把这个MappedByteBuffer直接写入SocketChannel，这样减少了数据复制，从而提高了性能。
 * <p>
 * 2、 重复使用的数据，比如HTTP的错误信息，例如404呀，这些信息是每次请求，响应数据都一样的，那么我们可以把这些固定的信息预先存放在Direct Buffer中
 * （当然部分修改Direct Buffer中的信息也可以，重要的是Direct Buffer要能被重复使用），这样把Direct Buffer直接写入SocketChannel就比写入Heap Buffer要快了。
 * <p>
 *
 * @auther zhihui.kzh
 * @create 29/8/1716:57
 */
public class Nio_ByteBufferTest {

    // ByteBuffer.wrap  将字节数组转成 HeapByteBuffer
    @Test
    public void testByteBuffer() {
        byte[] bytes = "abc".getBytes();

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        System.out.println(new String(byteBuffer.array()));

        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.get());
        System.out.println(byteBuffer.get());
    }

    // Heap Buffer则是分配在堆上的：ByteBuffer.allocate
    @Test
    public void testHeapByteBuffer() {

        ByteBuffer byteBuffer = ByteBuffer.allocate(100);

        byteBuffer.put((byte) 48);
        byteBuffer.put((byte) 97);
        byteBuffer.put((byte) 98);

        System.out.println(new String(byteBuffer.array()));


        System.out.println(byteBuffer.get(0));
        System.out.println(byteBuffer.get(1));
        System.out.println(byteBuffer.get(2));
    }

    // Direct Buffer 堆外内存：ByteBuffer.allocateDirect
    @Test
    public void testDirectByteBuffer() {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);

        byteBuffer.put((byte) 48);
        byteBuffer.put((byte) 97);
        byteBuffer.put((byte) 98);

        //  Direct Buffer 不允许使用  byteBuffer.array() 方法
//        System.out.println(new String(byteBuffer.array()));


        System.out.println(byteBuffer.get(0));
        System.out.println(byteBuffer.get(1));
        System.out.println(byteBuffer.get(2));
    }
}
