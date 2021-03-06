
#### 基础知识
* [Java 并发编程之 NIO 简明教程](http://mp.weixin.qq.com/s/MlWir1tJARx5UN_SS6iSHQ)
* [堆外内存之 DirectByteBuffer 详解](http://mp.weixin.qq.com/s/PdGO53sPT0ZyfHJXkzTMqA)

#### netty 书籍
* [netty in action 中文版 csdn在线](http://blog.csdn.net/qq_34590943/article/details/52253683)
* [netty in action 中文版 下载](http://download.csdn.net/download/xiaoyu5256/9799439)

#### netty 代码样例
* [netty-in-action-cn 章节代码样例](https://github.com/ReactivePlatform/netty-in-action-cn)
* [netty4-in-action netty代码样例](https://github.com/TiFG/netty4-in-action)
* netty样例代码在netty-example包中。

#### 内存池
```
在4.0.x版本中，UnpooledByteBufAllocator是默认的allocator，尽管其存在某些限制。
5.* 默认启用内存池。

netty4.0.x 启动内存池：
    bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);//关键是这句

option()是提供给NioServerSocketChannel用来接收进来的连接,也就是boss线程。
childOption()是提供给由父管道ServerChannel接收到的连接，也就是worker线程。


4.1.14 默认启用了内存池。
    对原消息不做处理，调用 ctx.fireChannelRead(msg)把原消息往下传，那不用做什么释放。
    将原消息转化为新的消息并调用 ctx.fireChannelRead(newMsg)往下传，那必须把原消息release掉。
    如果已经不再调用ctx.fireChannelRead(msg)传递任何消息，那更要把原消息release掉。

    在handler中打印：ctx.alloc().getClass().getSimpleName()， 结果为：PooledByteBufAllocator

    DefaultChannelConfig.getOption()  获取配置信息。
        getAllocator()  获取 Allocator
            ByteBufAllocator.DEFAULT
                ByteBufUtil.DEFAULT_ALLOCATOR   默认为 pooled
```

#### netty4入门
* EchoServer ，代码见： example包。
* helloworld  com.kk.netty4.helloworld
    * SimpleChannelInboundHandler 支持泛型，
        * 继承：ChannelInboundHandlerAdapter。
        * 自定义实现子类(HelloClientHandler，HelloServerHandler)设置泛型，对应自定义的encoder。
        * StringEncoder 对应 泛型为 String。表示对传递消息进行编码，继承ChannelOutboundHandlerAdapter。
        * StringDecoder 对应 泛型为 ByteBuf，解码结果为string，继承ChannelInboundHandlerAdapter。
        * HelloClientHandler，HelloServerHandler 对应类型为泛型为String，需要将String类型消息传递到StringEncoder， StringDecoder解码结果为String类型。
    * 如果一个channelPipeline中有多个channelHandler时，且这些channelHandler中有同样的方法时，例如channelActive方法，只会调用处在第一个的channelHandler中的channelActive方法，如果你想要调用后续的channelHandler的同名的方法就需要调用以“fire”为开头的方法了
    * SimpleChannelInboundHandler.acceptInboundMessage()方法检测参数是否和泛型类型一致。 只有类型一致才会调用channelRead0()方法。
        * 见：ChannelInboundHandlerTest
    * 常见编码类： CharsetUtil，如： CharsetUtil.UTF_8

* number  见com.kk.netty4.number
    * 自定义编解码，只允许输入int。
    * IntegerDecoder，IntegerEncoder

* object 对象传输， 见 com.kk.netty4.object
    * ObjectDecoder，ObjectEncoder
    * ObjectDecoder 继承：LengthFieldBasedFrameDecoder 拆包器。
    * 序列化方式：
        * CompactObjectInputStream
        * CompactObjectOutputStream
        * 继承 jdk序列化方式，修改序列化的header和classDescription部分。
        * ObjectEncoder 中先写长度占位符，然后再写自定义对象序列化，然后再设置头部长度占位符长度。

* hessian 对象传输，hessian序列化方式， 见 com.kk.netty4.hessian
    * HessianEncoder，HessianDecoder
    * HessianDecoder 继承：LengthFieldBasedFrameDecoder 拆包器。
    * HessianEncoder 中先写长度占位符，然后再写自定义对象hessian序列化，然后再设置头部长度占位符长度。

* http.helloword  见 com.kk.netty4.http.helloworld
    * netty搭建http服务
    * http 使用HttpServerCodec编解码，默认解码为：DefaultHttpRequest
    * HttpHelloWorldServerHandler ： hello world http服务
    * SimpleFileServerHandler ： 简单文件服务器。
        * 返回 DefaultFullHttpResponse， 包含header和content。
    * content-length 设置： HttpUtil.setContentLength(response, fileLength)
    * content-type 获取见：ContentTypeTest， 设置：esponse.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);


* http.file   http形式访问本地文件，见 com.kk.netty4.http.file
    * 下载文件服务器
    * Post请求是分两次收到的，一次是HttpRequest，只有Http 协议头，一次是HttpContent，内容是data form数据。
    * FullHttpRequest的协议头和Form数据是在一起的，不用分开读，之前分开接收的是DefaultHttpRequest与HttpContent，FullHttpRequest把两者合在一起了。
    * 返回内容：先返回 DefaultHttpResponse，再返回 HttpChunkedInput 。

```

1. Netty 中有两个方向的数据流，入站(ChannelInboundHandler)和出站 (ChannelOutboundHandler)之间有一个明显的区别:
    若数据是从用户应用程序到远程主机 则是“出站(outbound)”，相反若数据时从远程主机到用户应用程序则是“入站(inbound)”。

    ChannelInitializer 用来初始化 ChannelHandler，将自定义的各种 ChannelHandler 添加到 ChannelPipeline 中。

2. ChannelPipeline 的作用我们可以理解为用来管理 ChannelHandler 的一个容器，每个 ChannelHandler 处理各自的数据
    (例如入站数据只能由 ChannelInboundHandler 处理)，处理完成后将转换的数据放到 ChannelPipeline 中交给下 一个
    ChannelHandler 继续处理(fire开头方法)，直到最后一个 ChannelHandler 处理完成。

    ChannelPipeline 是 ChannelHandler 的一个列表;
        如果一个入站 I/O 事件被 触发，这个事件会从第一个开始依次通过 ChannelPipeline 中的 ChannelHandler;
        若是一 个入站 I/O 事件，则会从最后一个开始依次通过 ChannelPipeline 中的 ChannelHandler。
        被添加到 ChannelPipeline 的 ChannelHandler 将通过 IO-Thread 处理事件，这意味了必须不能有其他的 IO-Thread 阻塞来影响 IO 的整体处理;
          addFirst(...)，添加 ChannelHandler 在 ChannelPipeline 的第一个位置
          addBefore(...)，在 ChannelPipeline 中指定的 ChannelHandler 名称之前添加ChannelHandler
          addAfter(...)，在 ChannelPipeline 中指定的 ChannelHandler 名称之后添加ChannelHandler
          addLast(ChannelHandler...)，在 ChannelPipeline 的末尾添加 ChannelHandler
          remove(...)，删除 ChannelPipeline 中指定的 ChannelHandler
          replace(...)，替换 ChannelPipeline 中指定的 ChannelHandler

3. Netty 提供了抽象的事件基类称为 ChannelInboundHandlerAdapter 和 ChannelOutboundHandlerAdapter。
    每个都提供了在 ChannelPipeline 中通过调用相应的方 法将事件传递给下一个 Handler 的方法的实现。

4. SimpleChannelInboundHandler继承ChannelInboundHandlerAdapter，可以指定泛型，如：HelloClientHandler，只处理此类型的数据。

5 ChannelHandlerContext(ctx)
    每个 ChannelHandler 被添加到 ChannelPipeline 后，都会创建一个 ChannelHandlerContext 并与之创建的 ChannelHandler 关联绑定。
    ChannelHandlerContext 允许 ChannelHandler 与其他的 ChannelHandler 实现进行交互，这是相同 ChannelPipeline 的一部分。
    ChannelHandlerContext 不会改变添加到其中的 ChannelHandler，因此它是安 全的。

    @Sharable注解主要是用来标示一个ChannelHandler可以被安全地共享，即可以在多个Channel的ChannelPipeline中使用同一个ChannelHandler，而不必每一个ChannelPipeline都重新new一个新的ChannelHandler。

    在 Netty 目前基于 EventLoop 的线程模型中，Netty 是要求用户的ChannelHandler的实现必须要是线程安全的。
    这样，其便可以在不同的Channel的ChannelPipeline中安全的共享。所以，设计上为了避免非线程安全的ChannelHandler被错误地共享，
    所以 Netty 要求你标注一个ChannelHandler为@Shareable来指示它的线程安全性。

    从ctx中获取 pipeline：ctx.pipeline()
    判断pipeline中是否添加某ChanHandler：ctx.pipeline().get(XXXHandler.class) != null 表示添加了XXXHandler。


6.  Channel 生命周期四个不同的状态
    channelRegistered-->channelActive-->channelInactive-->channelUnregistered

7.1 使用 ChannelInboundHandler、 ChannelInboundHandlerAdapter、SimpleChannelInboundhandler 这三个中的一个来处理 接收消息，
    大多数时候使用 SimpleChannelInboundHandler 处理消息，使用 ChannelInboundHandlerAdapter 处理其他的“入站”事件或状态改变。


7.2 ChannelInboundHandler

   ChannelInboundHandler 提供了一些方法再接收数据或 Channel 状态改变时被调用。 下面是 ChannelInboundHandler 的一些方法:
        channelRegistered，ChannelHandlerContext 的 Channel 被注册到 EventLoop;
        channelUnregistered，ChannelHandlerContext 的 Channel 从 EventLoop 中注销
        channelActive，ChannelHandlerContext 的 Channel 已激活
        channelInactive，ChannelHanderContxt 的 Channel 结束生命周期
        channelRead，从当前 Channel 的对端读取消息
        channelReadComplete，消息读取完成后执行
        userEventTriggered，一个用户事件被处罚
        channelWritabilityChanged，改变通道的可写状态，可以使用 Channel.isWritable()检查
        exceptionCaught，重写父类 ChannelHandler 的方法，处理异常

   Netty 提供了一个实现了 ChannelInboundHandler 接口并继承 ChannelHandlerAdapter 的类:ChannelInboundHandlerAdapter。
   ChannelInboundHandlerAdapter 实现了 ChannelInboundHandler 的所有方法，作用就是处 理消息并将消息转发到 ChannelPipeline 中的下一个 ChannelHandler。
   ChannelInboundHandlerAdapter 的 channelRead 方法处理完消息后不会自动释放消息，
   若想自动释放收到的消息，可以使用 SimpleChannelInboundHandler<I>。

        内存池管理：
            对原消息不做处理，调用 ctx.fireChannelRead(msg)把原消息往下传，那不用做什么释放。
            将原消息转化为新的消息并调用 ctx.fireChannelRead(newMsg)往下传，那必须把原消息release掉。
            如果已经不再调用ctx.fireChannelRead(msg)传递任何消息，那更要把原消息release掉。

            MessageToMessageDecoder和ByteToMessageDecoder已经对原消息进行了release操作。

        //手动释放消息
        ReferenceCountUtil.release(msg);

7.3 ChannelOutboundHandler
    ChannelOutboundHandler 用来处理“出站”的数据消息。:
          bind，Channel 绑定本地地址
          connect，Channel 连接操作
          disconnect，Channel 断开连接
          close，关闭 Channel
          deregister，注销 Channel
          read，读取消息，实际是截获 ChannelHandlerContext.read()
          write，写操作，实际是通过 ChannelPipeline 写消息，Channel.flush()属性到实际通道
          flush，刷新消息到通道
    Netty 提供了 ChannelOutboundHandler 的实现: ChannelOutboundHandlerAdapter。
    ChannelOutboundHandlerAdapter 实现了父类的所有 方法，并且可以根据需要重写感兴趣的方法。


8. 解码器
   解码 器负责处理“入站”数据，编码器负责处理“出站”数据
     ByteToMessageDecoder
        对ByteBuf的解码。
        如：LineBasedFrameDecoder 继承：ByteToMessageDecoder
     MessageToByteEncoder
        对ByteBuf的编解码。

        使用样例：IntegerDecoder，IntegerEncoder

     MessageToMessage 自定义对象之间的互相编解码。
         StringEncoder 泛型为对应的输入：CharSequence，输出为ByteBuf，继承MessageToMessageEncoder，继承ChannelOutboundHandlerAdapter。
                ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset)
         StringDecoder 泛型为对应的输入：ByteBuf，输出为CharSequence，继承MessageToMessageDecoder，继承ChannelInboundHandlerAdapter。
                msg.toString(charset)

      ByteArrayEncoder 和 ByteArrayDecoder
        这两个类用来处理字节到字节的编码和解码。
8.2
  ByteToMessageCodec是一种组合，其等同于ByteToMessageDecoder和MessageToByteEncoder 的组合。
  MessageToMessageCodec 用于 message-to-message 的编码和解码，可以看成是 MessageToMessageDecoder 和 MessageToMessageEncoder 的组合体。

  需要同时实现：encode和decode方法。
  可以使用 CombinedChannelDuplexHandler 绑定解码器和编码器，样例如：IntegerCodec

8.3.1 拆包器
    拆包器基类为ByteToMessageDecoder，其内部有一个累加器，将每次新读取到的数据累加到本地字节容器，然后对累加后的本地字节容器中的数据进行拆包，拆成一个完整的业务数据包。
    默认情况下使用简单的MERGE_CUMULATOR累加器
        原理是每次都将读取到的数据通过内存拷贝的方式，拼接到一个大的字节容器中：ByteToMessageDecoder中的cumulation。
        private Cumulator cumulator = MERGE_CUMULATOR;


    具体实现类：
        LineBasedFrameDecoder -> 根据换行符\n或\r\n进行拆包
        DelimiterBasedFrameDecoder -> 根据用户定义的标识符进行拆包
        FixedLengthFrameDecoder --> 固定长度解码器，它可以按照指定的长度对消息进行解码（用途较少）
        LengthFieldBasedFrameDecoder -> 根据包头长度进行拆包，适用于私有协议解码

8.3.2 解析分隔符：
    DelimiterBasedFrameDecoder，解码器，接收 ByteBuf 由一个或多个分隔符拆分， 如 NULL 或换行符
    LineBasedFrameDecoder，解码器，接收 ByteBuf 以分割线结束，如"\n"和"\r\n"
         默认过滤分隔符，即服务端业务handler中收到的消息中把分隔符已经strip了。

    样例如下：
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter())); // 指定分隔符为 \n,\r\n
        pipeline.addLast("framer", new LineBasedFrameDecoder(8192, true, true)); // 换行分割，也可以这样写
8.4
    对Decoder来说，主要有两个顶层的抽象类，一个是从字节流到消息的ByteToMessageDecoder，一个是中间消息到业务消息的MessageToMessageDecoder。
        ByteToMessageDecoder放置在MessageToMessageDecoder前面，处理inbound事件。
        最后当ChannelPipeline到达后端的业务ChannelHandler时，拿到的消息是已经解码后的业务消息。


    编码的过程也是一样，提供两个顶层接口，
        MessageToMessageEncoder负责把业务消息转化成中间消息
        MessageToByteEncoder负责把中间消息/业务消息转化成字节流


9.1 IdleStateHandler 心跳机制
    IdleStateHandler这个类会根据你设置的超时参数的类型和值，循环去检测channelRead和write方法多久没有被调用了，
    如果这个时间超过了你设置的值，那么就会触发对应的事件，read触发read，write触发write，all触发all。
    如果超时了，则会调用userEventTriggered方法，且会告诉你超时的类型
    如果没有超时，则会循环定时检测

    样例如下：
    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds,allIdleTimeSeconds));
    参数默认为秒， 读空闲，写空闲，读写空闲。
    [DEMO](http://blog.csdn.net/linuu/article/details/51404264)

9.2
```


