package com.zxb.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * {@link Bootstrap} 引导客户端
 *
 * @author Mr.zxb
 * @date 2020-05-01
 **/
public class ClientBootstrapDemo {

    public static void main(String[] args) throws InterruptedException {

        // 创建引导 Bootstrap
        Bootstrap bootstrap = new Bootstrap();

        // 创建 NIO EventLoopGroup
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        bootstrap.group(eventLoopGroup)
                // 设置要使用的a Channel 实现
                .channel(NioSocketChannel.class)
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received data...");
                    }
                })
                .remoteAddress(new InetSocketAddress("www.baidu.com", 80));

        // 连接到远程主机
        ChannelFuture channelFuture = bootstrap.connect();

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("Connection established");
            } else {
                System.out.println("Connection attempt failed");
                future.cause().printStackTrace();
            }
        });
    }
}
