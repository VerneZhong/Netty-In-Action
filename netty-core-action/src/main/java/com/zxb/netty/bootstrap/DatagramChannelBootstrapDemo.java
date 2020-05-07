package com.zxb.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * 基于 UDP 无连接协议的 {@link DatagramChannel} 引导的 {@link Bootstrap}
 * @author Mr.zxb
 * @date 2020-05-07 20:18:15
 */
public class DatagramChannelBootstrapDemo {

    public static void main(String[] args) {

        // 创建 Bootstrap 的实例以创建和绑定新的数据报 Channel
        Bootstrap bootstrap = new Bootstrap();

        // 创建 NIO EventLoopGroup
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        // 设置 EventLoopGroup，其提供了用以处理 Channel 事件的 EventLoop
        bootstrap.group(eventLoopGroup)
                // 指定 Channel 的实现
                .channel(NioDatagramChannel.class)
                // 设置以处理 Channel 的 I/O 以及数据的 ChannelInboundChannel
                .handler(new SimpleChannelInboundHandler<DatagramPacket>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket) throws Exception {
                        // Do something with the packet
                        System.out.println("接收的数据包：" + datagramPacket.toString());
                    }
                });

        // 调用 bind() 方法，因为该协议是无连接的
        bootstrap.bind(new InetSocketAddress(0)).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Channel bound.");
            } else {
                System.out.println("Bind attempt failed.");
                channelFuture.cause().printStackTrace();
            }
        });

        // 优雅的关闭资源
        // shutdownGracefully 将释放所有的资源，并且关闭所有的当前正在使用中的 Channel
        Future<?> future = eventLoopGroup.shutdownGracefully();

        // block until the group has shutdown
        future.syncUninterruptibly();
    }
}
