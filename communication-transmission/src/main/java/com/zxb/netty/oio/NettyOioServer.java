package com.zxb.netty.oio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * 基于Netty的同步服务端
 *
 * @author Mr.zxb
 * @date 2020-04-22
 **/
public class NettyOioServer {

    private void server(int port) {
        ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", StandardCharsets.UTF_8));

        // todo Netty 的 OIO 已不推荐使用，推荐 use NIO / EPOLL / KQUEUE transport.
        EventLoopGroup group = new OioEventLoopGroup();

        try {
            // 创建 ServerBootstrap
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(group)
                    // 使用 OioEventLoopGroup 以允许阻塞模式（同步I/O）
                    .channel(OioServerSocketChannel.class)
                    // 设置监听端口
                    .localAddress(new InetSocketAddress(port))
                    // 设置 ChannelHandler 处理事件
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 添加一个 ChannelInboundHandlerAdapter 以拦截和处理事件
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 将消息写到客户端，并添加 ChannelFutureListener，以便消息一被写完就关闭了
                                    ctx.writeAndFlush(byteBuf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            // 绑定服务器以接受连接
            ChannelFuture channelFuture = bootstrap.bind().sync();
            // 监听关闭连接
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放所有的资源
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
