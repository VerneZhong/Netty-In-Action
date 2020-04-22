package com.zxb.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Echo Netty Client
 *
 * @author
 * @date 2020-04-21
 **/
public class EchoNettyClient {

    /**
     * 服务端地址
     */
    private final String host;

    /**
     * 服务端端口
     */
    private final int port;

    public EchoNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void start() {
        EchoClientHandler echoClientHandler = new EchoClientHandler();

        // 1. 创建 Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        // 2. 指定 EventLoopGroup 以处理客户端事件；需要适用于 NIO 的实现
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(eventLoopGroup)
                    // 3. 设置 NIO Channel 类型
                    .channel(NioSocketChannel.class)
                    // 4. 设置服务器的地址
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 5. 添加 Client ChannelHandler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(echoClientHandler);
                        }
                    });
            // 6. 连接远程服务器，阻塞等待直到连接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 7. 阻塞直到 Channel 关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭线程池并且释放所有的资源
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EchoNettyClient("127.0.0.1", 8888).start();
    }
}
