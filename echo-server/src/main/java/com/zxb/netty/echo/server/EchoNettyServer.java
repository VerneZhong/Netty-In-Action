package com.zxb.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Echo Netty Server
 *
 * @author
 * @date 2020-04-21
 **/
public class EchoNettyServer {

    /**
     * 监听的端口
     */
    private final int port;

    public EchoNettyServer(int port) {
        this.port = port;
    }

    private void start() {
        // 1.创建 ServerBootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 2.创建 基于NIO NioEventLoopGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(eventLoopGroup)
                    // 3.指定所使用的 NIO 传输 Channel
                    .channel(NioServerSocketChannel.class)
                    // 4.使用指定的端口设置监听地址
                    .localAddress(new InetSocketAddress(port))
                    // 5.添加一个 EchoServerHandler 到子 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // EchoServerHandler 被标注为 @Shareable，所以我们可以总是使用同样的实例
                            // ChannelHandler 实现
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 6. 异步地绑定服务器，调用 sync() 方法阻塞等待直到绑定完成
            ChannelFuture sync = bootstrap.bind().sync();
            // 7. 获取 Channel 的 CloseFuture，并且阻塞当前线程直到它完成
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // 8. 关闭 EventLoopGroup，释放所有的资源
                eventLoopGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new EchoNettyServer(8888).start();
    }
}
