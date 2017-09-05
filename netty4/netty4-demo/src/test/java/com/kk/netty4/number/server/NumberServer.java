package com.kk.netty4.number.server;

import com.kk.netty4.number.IntegerCodec;
import com.kk.netty4.number.IntegerDecoder;
import com.kk.netty4.number.IntegerEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetAddress;

/**
 * @auther zhihui.kzh
 * @create 5/9/1711:48
 */
public class NumberServer {
    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7878;

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);

            // socket等待队列长度
//            b.option(ChannelOption.SO_BACKLOG, 100);

//            b.handler(new LoggingHandler(LogLevel.INFO));

            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

//                    pipeline.addLast("decoder", new IntegerDecoder());
//                    pipeline.addLast("encoder", new IntegerEncoder());

                    // 使用编解码器
                    pipeline.addLast("codec", new IntegerCodec());


                    // 自己的逻辑Handler
                    pipeline.addLast("handler", new SimpleChannelInboundHandler<Integer>() {

                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Integer msg) throws Exception {
                            // 收到消息直接打印输出
                            System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

                            // 返回客户端消息 - 我已经接收到了你的消息
                            ctx.writeAndFlush(msg + 100);
                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {

                            System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

                            super.channelActive(ctx);
                        }
                    });
                }
            });

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(portNumber).sync();
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
