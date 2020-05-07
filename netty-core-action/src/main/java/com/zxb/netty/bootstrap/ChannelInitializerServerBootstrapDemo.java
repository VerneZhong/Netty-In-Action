package com.zxb.netty.bootstrap;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;

/**
 * {@link ServerBootstrap} 引导和使用 {@link ChannelInitializer}
 *
 * @author Mr.zxb
 * @date 2020-05-07
 **/
public class ChannelInitializerServerBootstrapDemo {

    public static void main(String[] args) throws InterruptedException {

        // 创建 ServerBootstrap 以创建和绑定新的 Channel
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                // 指定 Channel 的实现
                .channel(NioServerSocketChannel.class)
                // 注册一个 ChannelInitializer 的实现实例来设置 ChannelPipeline
                .childHandler(new ChannelInitializerImpl());

        // 绑定到地址
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8080)).sync();
        System.out.println("Server bind...");

        // 返回该通道关闭时将通知的 ChannelFuture。此方法始终返回相同的Future实例
        channelFuture.channel().closeFuture().sync();
    }

    private static class ChannelInitializerImpl extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            // 将所需的 ChannelHandler 添加到 ChannelPipeline
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new HttpClientCodec())
                    .addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        }
    }
}
