package com.zxb.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 基于Java的NIO的服务端
 *
 * @author Mr.zxb
 * @date 2020-04-22
 **/
public class PlainNioServer {

    private void server(int port) throws IOException {
        ServerSocketChannel socketChannel = ServerSocketChannel.open();
        // 开启异步
        socketChannel.configureBlocking(false);
        ServerSocket socket = socketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        // 绑定监听端口
        socket.bind(inetSocketAddress);

        // 打开 Selector 来处理 Channel
        Selector selector = Selector.open();

        // 将 ServerSocket 注册到 Selector 以接受连接
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer byteBuffer = ByteBuffer.wrap("Hi!\r\n".getBytes());

        for (; ; ) {
            // 等待需要处理的新事件；阻塞将一直持续到下一个传入事件
            try {
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        // 获取所有接收事件到 SelectionKey 实例
        Set<SelectionKey> selectionKeys = selector.selectedKeys();

        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        while (keyIterator.hasNext()) {
            SelectionKey selectionKey = keyIterator.next();
            keyIterator.remove();

            // 检查事件是否是一个新到已经就绪可以被接受的连接
            if (selectionKey.isAcceptable()) {
                ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();

                SocketChannel client = server.accept();
                client.configureBlocking(false);
                client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, byteBuffer.duplicate());
                System.out.println("Accepted connection from " + client);
            }

            // 检查socket 是否已经准备好写数据
            if (selectionKey.isWritable()) {
                SocketChannel client = (SocketChannel) selectionKey.channel();

                ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                while (buffer.hasRemaining()) {
                    // 将数据写到已连接的客户端
                    if (client.write(buffer) == 0) {
                        break;
                    }
                }
                // 关闭连接
                client.close();
            }
        }

    }
}
