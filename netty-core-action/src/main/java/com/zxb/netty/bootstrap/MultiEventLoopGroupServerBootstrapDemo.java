package com.zxb.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * {@link ServerBootstrap} 多个 {@link EventLoopGroup} 的服务端引导。
 * <p>
 *     当服务器处理一个客户端的请求，该请求需要它充当第三方系统的客户端。当一个应用程序必须要和组织现有的系统集成时，
 *     就可能会发生这种情况。这种情况下，将需要从已经被接受的子 {@link Channel} 中引导一个客户端 {@link Channel}。
 *     解决方案：通过将已被接受的子 {@link Channel} 的 {@link EventLoop} 传递给 {@link Bootstrap} 的 {@link Bootstrap#group(EventLoopGroup)}
 *     方法来共享该 {@link EventLoop}。因为分配给 {@link EventLoop} 的所有 {@link Channel} 都使用同一个线程，所以
 *     避免来额外的线程创建，避免上下午切换带来的性能问题。
 * </p>
 * @author Mr.zxb
 * @date 2020-05-06
 **/
public class MultiEventLoopGroupServerBootstrapDemo {

    public static void main(String[] args) {

        // 创建 ServerBootstrap
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        serverBootstrap.group(bossGroup, workGroup)
                // 指定的 Channel 的实现
                .channel(NioServerSocketChannel.class)
                // 设置用于处理已被接受的子 Channel 的 I/O 和数据的 ChannelInboundHandler
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

                    private ChannelFuture channelFuture;

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        channelFuture = childChannelConnection(ctx);

                        channelFuture.addListener((ChannelFutureListener) future -> {
                            if (channelFuture.isSuccess()) {
                                // 连接远程节点成功时
                                System.out.println("child channel connection remote node success.");
                            } else {
                                System.out.println("child channel connection remote node failed.");
                                channelFuture.cause().printStackTrace();
                            }
                        });
                    }

                    private ChannelFuture childChannelConnection(ChannelHandlerContext ctx) {
                        // 创建 Bootstrap类的实例以连接远程主机
                        Bootstrap bootstrap = new Bootstrap();

                        // 指定 Channel 的实现
                        bootstrap.channel(NioSocketChannel.class)
                                // 为入站的 I/O 设置 ChannelInboundHandler
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                        System.out.println("Received data.");
                                    }
                                });

                        // 使用与分配给已被接受的子 Channel 相同的 EventLoop
                        bootstrap.group(ctx.channel().eventLoop());

                        // 连接到远程节点
                        return bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        if (channelFuture.isDone()) {
                            // 当连接已完成时，执行一些数据操作（如代理）
                            // do something with the data
                            System.out.println(msg.toString(StandardCharsets.UTF_8));
                        }
                    }
                });

        // 通过配置好的 ServerBootstrap 绑定该 ServerSocketChannel
        ChannelFuture channelFuture = serverBootstrap.bind(new InetSocketAddress(8080));

        channelFuture.addListener((ChannelFutureListener) future -> {
            if (channelFuture.isSuccess()) {
                System.out.println("Server bound.");
            } else {
                System.out.println("Server bind attempt failed.");
                channelFuture.cause().printStackTrace();
            }
        });
    }
}
