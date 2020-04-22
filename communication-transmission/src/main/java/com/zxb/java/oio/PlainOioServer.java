package com.zxb.java.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 基于Java的IO的服务端
 *
 * @author
 * @date 2020-04-22
 **/
public class PlainOioServer {

    private void server(int port) throws IOException {
        // 绑定指定端口
        ServerSocket serverSocket = new ServerSocket(port);

        for (; ; ) {
            // 阻塞接收客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket);

            // 创建新线程来处理客户端连接，并启动
            new Thread(() -> {
                OutputStream out;
                try {
                    out = socket.getOutputStream();
                    // 将消息写给客户端
                    out.write("Hi!\r\n".getBytes(StandardCharsets.UTF_8));
                    out.flush();
                    // 关闭连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
