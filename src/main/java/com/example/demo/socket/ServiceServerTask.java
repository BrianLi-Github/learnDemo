package com.example.demo.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceServerTask implements Runnable {

    private Socket socket;
    InputStream inputStream = null;
    OutputStream outputStream = null;

    public ServiceServerTask(Socket socket) {
        this.socket = socket;
    }

    /**
     * 业务逻辑，和socket客户端进行数据交互
     */
    @Override
    public void run() {
        try {
            //从socket连接中获取到client之间的网络通信流
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            //从网络通信流中获取客户端发过来的数据
            //注： socket inputStream的读数据的方法都是阻塞的
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println("Thread --> " + Thread.currentThread().getName() + " get request from client: " + line);
                //获取客户端的请求，进行相应的业务处理
                BusinessesHandler handler = new BusinessesHandler();
                String handlerResult = handler.handler(line);

                //将处理的结构写到socket的输出流中，以便发给客户端
                PrintWriter writer = new PrintWriter(outputStream);
                writer.println(handlerResult);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
