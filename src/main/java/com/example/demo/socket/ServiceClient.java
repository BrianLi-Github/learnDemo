package com.example.demo.socket;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class ServiceClient {
    public static void main(String[] args) throws IOException, InterruptedException {

        // 向服务器发出请求建立连接
        Socket socket = new Socket("localhost", 8899);
        //从socket中获取输入输出流
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        //向socket流中写入数据
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("food");
        pw.flush();


        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String line = br.readLine();
        System.out.println(line);

        Thread.sleep(5000);
        pw.println("ice");
        pw.flush();
        line = br.readLine();
        System.out.println(line);

        inputStream.close();
        outputStream.close();
        socket.close();

    }
}
