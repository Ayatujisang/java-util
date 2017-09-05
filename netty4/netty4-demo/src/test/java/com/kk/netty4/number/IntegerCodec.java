package com.kk.netty4.number;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 使用 CombinedChannelDuplexHandler 来结合解码器和编码器
 *
 * @auther zhihui.kzh
 * @create 5/9/1713:08
 */
public class IntegerCodec extends CombinedChannelDuplexHandler<IntegerDecoder, IntegerEncoder> {

    public IntegerCodec() {
        super(new IntegerDecoder(), new IntegerEncoder());
    }
}
