package com.example.demo.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 */
public class ServiceServer {
    public static void main(String[] args) throws IOException {

        // 创建一个ServerSocket对象，绑定到本机的8899端口上
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("localhost", 8899));

        //接受客户端的请求；accept是一个阻塞方法，会一直等待，到有客户端请求连接才返回
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        while (true) {
            Socket socket = server.accept();
            executorService.submit(new ServiceServerTask(socket));
        }
    }
}
