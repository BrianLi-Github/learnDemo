package com.example.demo.nio;

/**
 * 服务端
 */
public class TimeServer {

    public static void main(String[] args) {
        int port = 8080;

        new Thread(new MultiplexerTimeHandler(port)).start();
    }
}
