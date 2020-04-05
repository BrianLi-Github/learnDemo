package com.example.demo.netty.server;

import com.example.demo.netty.bean.MessageObjDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 配置服务器
 * 1.创建ServerBootstrap引导
 * 2.创建NioEventLoopGroup事件组
 * 3.指定通道类型为NioServerSocketChannel
 * 4.绑定监听端口
 *  --> childHandler 流式处理
 *      --> a. 将客户端发送的信息进行解码，将ByteBuf转成object对象，在给到下一个InboundHandler
 *      --> b. InboundHandler拿到服务器发送过来的对象进行处理
 */
public class NettyServer {

    private int port;

    NettyServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = null;
        try {
            //创建ServerBootstrap来引导绑定和启动服务器端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //创建NioEventLoopGroup对象来处理事件，如接受新连接、接收数据、写数据等等
            nioEventLoopGroup = new NioEventLoopGroup();
            //指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
            //设置InetSocketAddress让服务器监听某个端口已等待客户端连接。
            serverBootstrap.group(nioEventLoopGroup).channel(NioServerSocketChannel.class).localAddress("localhost", port).childHandler(new ChannelInitializer<Channel>() {
                //设置childHandler执行所有的连接请求
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    //注册解码的handler
                    ch.pipeline().addLast(new MessageObjDecoder());  //IN1  反序列化
                    //添加一个入站的handler到ChannelPipeline
                    ch.pipeline().addLast(new ServerHandler1());   //IN2
                }
            });
            // 最后绑定服务器等待直到绑定完成，调用sync()方法会阻塞直到服务器完成绑定,然后服务器等待通道关闭，因为使用sync()，所以关闭操作也会被阻塞。
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } finally {
            if (nioEventLoopGroup != null) {
                nioEventLoopGroup.shutdownGracefully().sync();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new NettyServer(10001).run();
    }
}
