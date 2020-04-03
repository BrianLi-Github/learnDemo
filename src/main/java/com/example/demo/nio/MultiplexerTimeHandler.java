package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 选择器(多路复用器)处理类
 * SelectionKey.isValid();
 * SelectionKey.isAcceptable();
 * SelectionKey.isReadable();
 * SelectionKey.isConnectable();
 */
public class MultiplexerTimeHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    MultiplexerTimeHandler(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {

        // 循环监听通道channel中事件
        while (!stop) {
            try {
                //获取选择器，timeout 1000毫秒
                selector.select(1000);
                //获取监听的事件
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();   //获取之后就remove掉，表示已经处理了
                    try {
                        //handle 监听到的事件
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        // 多路复用器关闭后，所有注册在上面的Channel和Pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }
        if (key.isAcceptable()) {
            // 处理新接入的请求消息
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            // 往selector注册一个read监听
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
        //监听到可读取数据的事件
        if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel(); // socket channel 可读可写，一端写，对端就可通过read方法获取到
            ByteBuffer readBuffer = ByteBuffer.allocate(1024); //分配内存
            int readBytes = socketChannel.read(readBuffer);
            /**
             * readBytes = 0 读取到0个字节
             * readBytes > 0 读取到数据
             * readBytes < 0 对端链路被关闭了
             */
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("服务器收到请求数据: " + body);
                String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
                        System.currentTimeMillis()).toString()
                        : "BAD ORDER";
                //使用该通道给对端发送数据 --->  即，往该通道写入数据
                doWrite(socketChannel, currentTime);
            } else if (readBytes < 0) {
                // 对端链路关闭
                key.cancel();
                socketChannel.close();
            }
        }
    }

    private void doWrite(SocketChannel channel, String responseData)
            throws IOException {
        if (responseData != null && responseData.trim().length() > 0) {
            byte[] bytes = responseData.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
