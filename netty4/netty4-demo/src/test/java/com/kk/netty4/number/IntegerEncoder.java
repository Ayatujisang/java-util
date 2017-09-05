package com.kk.netty4.number;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * int 转ByteBuf
 * <p>
 * MessageToByteEncoder 输入需要指定泛型。
 *
 * @auther zhihui.kzh
 * @create 5/9/1711:53
 */
public class IntegerEncoder extends MessageToByteEncoder<Integer> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, ByteBuf out) throws Exception {
        out.writeInt(msg);
    }
}
