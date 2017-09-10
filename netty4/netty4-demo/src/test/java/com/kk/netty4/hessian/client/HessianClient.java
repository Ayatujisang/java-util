package com.kk.netty4.hessian.client;

import com.kk.model.User;
import com.kk.netty4.hessian.HessianDecoder;
import com.kk.netty4.hessian.HessianEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @auther zhihui.kzh
 * @create 5/9/1712:05
 */
public class HessianClient {
    public static String host = "127.0.0.1";
    public static int port = 7878;

    public static void main(String[] args) throws InterruptedException, IOException {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast("decoder", new HessianDecoder(Integer.MAX_VALUE));
                            pipeline.addLast("encoder", new HessianEncoder());

                            pipeline.addLast("handler", new SimpleChannelInboundHandler<User>() {

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, User msg) throws Exception {
                                    // 收到消息直接打印输出
                                    System.out.println("Server Say : " + msg);
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {

                                    System.out.println("Client active ");
                                    super.channelActive(ctx);
                                }
                            });
                        }
                    });

            ChannelFuture future = b.connect(host, port).sync();

            Channel ch = future.channel();
            // 控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (; ; ) {
                String line = in.readLine();
                if (line == null || "".equals(line)) {
                    continue;
                }
                String[] conts = StringUtils.split(line);
                User user = new User();
                user.setId(Integer.valueOf(conts[0]));
                user.setName(conts[1]);

                ch.writeAndFlush(user);
            }

        } finally {
            group.shutdownGracefully();
        }
    }
}
