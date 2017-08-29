
#### [netty in action 中文版 csdn在线](http://blog.csdn.net/qq_34590943/article/details/52253683)
#### [netty in action 中文版 下载](http://download.csdn.net/download/xiaoyu5256/9799439)


#### [netty-in-action-cn 章节代码样例](https://github.com/ReactivePlatform/netty-in-action-cn)

#### [netty4-in-action netty代码样例](https://github.com/TiFG/netty4-in-action)

#### netty样例代码在netty-example包中。


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