package com.zxb.netty.oom;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * 模拟客户端启动一个线程向服务端循环发送请求消息，
 * 在实际项目中，根据业务 QPS 规划、客户端处理性能、网络带宽、链路数、消息平均码流大小等综合因素计算并设置高水位 {@link ChannelConfig#setWriteBufferHighWaterMark(int)} 值，
 * 利用高水位做消息发送速度等流控，既可以保护自身，同时又能减轻服务端的压力，防止服务端被压挂
 *
 * @author Mr.zxb
 * @date 2020-06-08 14:13
 */
public class NettyOomClient {

    public void start() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress("127.0.0.1", 8080)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<Object>() {
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    // 设置高低水位机制，做流量控制，防止将服务器队列压满，造成消息积压
                                    ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
                                    new Thread(() -> {
                                        try {
                                            TimeUnit.SECONDS.sleep(3);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        while (true) {
                                            if (ctx.channel().isWritable()) {
                                                ctx.writeAndFlush(Unpooled.wrappedBuffer("Netty OOM example".getBytes()));
                                            } else {
                                                System.out.println("The write queue is busy: " + ctx.channel().unsafe().outboundBuffer().nioBufferCount());
                                            }
                                        }
                                    }).start();
                                }

                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

                                }
                            });
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyOomClient().start();
    }
}
