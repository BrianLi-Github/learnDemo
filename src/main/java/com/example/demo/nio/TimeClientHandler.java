package com.example.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TimeClientHandler implements Runnable {

    private Selector selector;
    private SocketChannel socketChannel;
    private String host;
    private int port;

    private volatile boolean stop;

    TimeClientHandler(String host, int port) {
        try {
            this.host = host;
            this.port = port;
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
                        //处理监听到的事件
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


    private void doConnect() throws IOException {
        // 如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            return;
        }
        if (key.isConnectable()) {
            // 判断是否连接成功
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.finishConnect()) {
                // 连接成功，可以往channel中写入输入，并往selector注册一个read监听，以便获取服务端返回的数据
                doWrite(socketChannel);
                socketChannel.register(selector, SelectionKey.OP_READ);
            } else {
                System.exit(1);
            }
        }
        //监听到可读取数据的事件，demo这里也就是表示从服务端获取到返回的数据，则标识该Channel的任务完成并退出程序
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
                System.out.println("收到服务端的返回数据: " + body);
                stop = true; //这里的程序只做到：有数据从服务器返回则结束
            } else if (readBytes < 0) {
                // 对端链路关闭
                key.cancel();
                socketChannel.close();
            }
        }
    }

    private void doWrite(SocketChannel channel)
            throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("Send order 2 server succeed.");
        }
    }
}
