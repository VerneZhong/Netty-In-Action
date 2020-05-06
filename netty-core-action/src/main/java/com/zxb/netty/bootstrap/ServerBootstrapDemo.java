package com.zxb.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * {@link ServerBootstrap} 引导服务端
 *
 * @author Mr.zxb
 * @date 2020-05-06
 **/
public class ServerBootstrapDemo {

    public static void main(String[] args) {

        // 创建 ServerBootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 创建 NioEventLoopGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 设置 EventLoopGroup，其提供了用于处理 Channel 事件的 EventLoop
        bootstrap.group(eventLoopGroup)
                // 指定要使用的 Channel 实现
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子 Channel  的 I/O 及数据的 ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<SocketChannel>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, SocketChannel msg) throws Exception {
                        System.out.println("Received data...");
                    }
                });

        // 通过配置好的 ServerBootstrap 的实例绑定该 Channel
        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(8080));

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("Server bound.");
            } else {
                System.out.println("Server bound attempt failed.");
                channelFuture.cause().printStackTrace();
            }
        });
    }
}
