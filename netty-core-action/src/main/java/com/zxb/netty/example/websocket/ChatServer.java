package com.zxb.netty.example.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * 引导类
 * @author Mr.zxb
 * @date 2020-05-18
 **/
public class ChatServer {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);

    private final EventLoopGroup group = new NioEventLoopGroup();

    private Channel channel;

    private final int port = 8080;

    /**
     * Server 启动入口
     * @return
     */
    public ChannelFuture start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createChannelInitializer(channelGroup))
                .bind(new InetSocketAddress(port)).syncUninterruptibly();
        System.out.println("Chat Server started. http://localhost:" + port);
        channel = channelFuture.channel();
        return channelFuture;
    }

    /**
     * 创建 ChannelInitializer
     * @param channelGroup
     * @return
     */
    protected ChannelInitializer<Channel> createChannelInitializer(ChannelGroup channelGroup) {
        return new ChatServerInitializer(channelGroup);
    }

    /**
     * 处理服务器关闭，并释放所有的资源
     */
    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        ChatServer chatServer = new ChatServer();
        ChannelFuture channelFuture = chatServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));

        channelFuture.channel().closeFuture().syncUninterruptibly();
    }
}
