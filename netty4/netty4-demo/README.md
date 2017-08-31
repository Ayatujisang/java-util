
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

#### netty4入门
* helloworld  com.kk.netty4.helloworld
    * SimpleChannelInboundHandler 支持泛型，
        * 继承：ChannelInboundHandlerAdapter。
        * 自定义实现子类(HelloClientHandler，HelloServerHandler)设置泛型，对应自定义的encoder。
        * StringEncoder 对应 泛型为 String。表示对传递消息进行编码。
        * StringDecoder 对应 泛型为 ByteBuf，解码结果为string。
        * HelloClientHandler，HelloServerHandler 对应类型为泛型为String，需要将String类型消息传递到StringEncoder， StringDecoder解码结果为String类型。
    * 如果一个channelPipeline中有多个channelHandler时，且这些channelHandler中有同样的方法时，例如channelActive方法，只会调用处在第一个的channelHandler中的channelActive方法，如果你想要调用后续的channelHandler的同名的方法就需要调用以“fire”为开头的方法了
    * SimpleChannelInboundHandler.acceptInboundMessage()方法检测参数是否和泛型类型一致。 只有类型一致才会调用channelRead0()方法。
        * 见：ChannelInboundHandlerTest


```
1. Netty 中有两个方向的数据流，入站(ChannelInboundHandler)和出站 (ChannelOutboundHandler)之间有一个明显的区别:
    若数据是从用户应用程序到远程主机 则是“出站(outbound)”，相反若数据时从远程主机到用户应用程序则是“入站(inbound)”。

2. ChannelPipeline 的作用我们可以理解为用来管理 ChannelHandler 的一个容器，每个 ChannelHandler 处理各自的数据
    (例如入站数据只能由 ChannelInboundHandler 处理)，处理完成后将转换的数据放到 ChannelPipeline 中交给下 一个
    ChannelHandler 继续处理(fire开头方法)，直到最后一个 ChannelHandler 处理完成。

3. Netty 提供了抽象的事件基类称为 ChannelInboundHandlerAdapter 和 ChannelOutboundHandlerAdapter。
    每个都提供了在 ChannelPipeline 中通过调用相应的方 法将事件传递给下一个 Handler 的方法的实现。

4. SimpleChannelInboundHandler继承ChannelInboundHandlerAdapter，可以指定泛型，如：HelloClientHandler，只处理此类型的数据。
```

