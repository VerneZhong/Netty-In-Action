package com.zxb.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * 使用 Netty {@link ChannelOption} 的 {@link Bootstrap}示例
 *
 * @author Mr.zxb
 * @date 2020-05-07A
 **/
public class ChannelOptionBootstrapDemo {

    public static void main(String[] args) {

        // 创建一个 AttributeKey 以标识该属性
        AttributeKey<Long> time = AttributeKey.valueOf("time");

        // 创建一个 Bootstrap 类的实例以创建客户端 Channel 并连接它们
        Bootstrap bootstrap = new Bootstrap();

        // 设置 EventLoopGroup，提供了用以处理 Channel事件的 EventLoop
        bootstrap.group(new NioEventLoopGroup())
                // 指定 Channel 的 NIO 实现
                .channel(NioSocketChannel.class)
                // 设置用以处理 Channel 的 I/O 以及数据的 ChannelInboundHandler
                .handler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        // 使用 AttributeKey 获取属性值
                        Long currentTime = ctx.channel().attr(time).get();
                        // do something with the variable
                        System.out.println("当前时间：" + currentTime);
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        System.out.println("Received data: " + msg.toString());
                    }
                })
                // 设置 ChannelOption，其将在 connect() 或者 bind() 方法被调用时被设置到已经创建的 Channel 上
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                // 存储该 id 属性
                .attr(time, System.currentTimeMillis());

        // 使用配置好的 Bootstrap 实例连接到远程主机
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("www.baidu.com", 80));

        // 不间断同步
        channelFuture.syncUninterruptibly();
    }
}
