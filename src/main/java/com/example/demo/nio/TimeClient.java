package com.example.demo.nio;

/**
 * 客户端
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        String host = "127.0.0.1";

        new Thread(new TimeClientHandler(host, port)).start();
    }
}
