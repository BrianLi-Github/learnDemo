package com.example.demo.netty.client;


import com.example.demo.netty.bean.MessageObjEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;

/**
 * 新建客户端
 * 1.连接服务器
 * 2.写数据到channel中
 * 3.等待服务器返回的数据
 * 4.关闭连接
 * <p>
 * <p>
 * 1. 创建Bootstrap引导，启动客户端
 * 2. 创建EventLoopGroup对象并设置到Bootstrap中
 * --> EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
 * 3. 创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
 * 4. 创建handler
 * --> a.InboundHandler，当连接成功后往ChannelHandlerContext写入需要传送的数据信息
 * --> b.OutboundHandler，将信息Object进行编码，即将对象转换成ByteBuf
 */
public class NettyClient {

    private String host;
    private int port;

    NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup nioEvenLoopGroup = null;
        try {
            // 1.创建Bootstrap引导，启动客户端
            Bootstrap bootstrap = new Bootstrap();
            // 2.创建EventLoopGroup对象并设置到Bootstrap中
            nioEvenLoopGroup = new NioEventLoopGroup();
            // 3.创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
            bootstrap.group(nioEvenLoopGroup).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port)).handler(new ChannelInitializer() {

                // 往ChannelHandlerContext写入需要传送给客户端的数据
                // 在OutBoundHandler中，将context中的对象数据进行编码，即将对象转换成功ByteBuf，以便服务器端从ByteBuf中转回到Object
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new MessageObjEncoder());   // outBound，主要将InboundHandler中的MessageObject转换成ByteBuf
//                    channel.pipeline().addLast(new StringDecoder());
                    channel.pipeline().addLast(new ClientHandler());   // inbound， 只要初始化需要发送的消息对象
                }
            });
            // 4.调用Bootstrap.connect()来连接服务器
            ChannelFuture future = bootstrap.connect().sync(); // 调用sync() ，则会阻塞，知道成功才继续往下执行代码
            // 5.最后关闭EventLoopGroup来释放资源
            future.channel().closeFuture().sync();
        } finally {
            if (nioEvenLoopGroup != null) {
                nioEvenLoopGroup.shutdownGracefully().sync();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyClient("127.0.0.1", 10001).run();
    }
}
