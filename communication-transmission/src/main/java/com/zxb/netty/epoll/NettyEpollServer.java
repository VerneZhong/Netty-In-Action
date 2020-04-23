package com.zxb.netty.epoll;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 基于Linux内核的Epoll非阻塞高性能传输，仅适用于Linux OS平台
 *
 * @author Mr.zxb
 * @date 2020-04-23
 **/
public class NettyEpollServer {

    private void server(int port) {
        // 传输的内容
        ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", StandardCharsets.UTF_8));

        // 创建 ServerBootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();

        // 创建 EpollEventLoopGroup
        EpollEventLoopGroup group = new EpollEventLoopGroup();

        try {
            // 添加 EventLoopGroup
            bootstrap.group(group)
                    // 设置 EpollServerSocketChannel
                    .channel(EpollServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加一个 ChannelInboundHandlerAdapter 以拦截和处理事件
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加 ChannelFutureListener，以便消息一被写完就关闭了
                                    ctx.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            // 绑定本地端口并阻塞监听
            ChannelFuture channelFuture = bootstrap.bind().sync();

            // 阻塞监听关闭连接
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
