package com.zxb.netty.close.jdk;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * Netty 基于 Java ShutdownHook 优雅退出机制
 *
 * @author Mr.zxb
 * @date 2020-06-01 11:05
 */
public class NettyShutdownGracefullyAsJdkShutdownHook {

    private NioEventLoopGroup eventLoopGroup;

    public void start() {
        try {
            eventLoopGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(8080)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                        }
                    });
            serverBootstrap.bind().sync();
            System.out.println("Netty Server start...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 优雅停机
     */
    public void shutdown() {
        System.out.println("ShutdownHook execute start...");
        System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
        eventLoopGroup.shutdownGracefully();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ShutdownHook execute end...");
    }

    public static void main(String[] args) {
        NettyShutdownGracefullyAsJdkShutdownHook server =
                new NettyShutdownGracefullyAsJdkShutdownHook();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.shutdown();
        }));
    }
}
