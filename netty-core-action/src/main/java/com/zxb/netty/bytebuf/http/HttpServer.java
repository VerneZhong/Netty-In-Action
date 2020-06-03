package com.zxb.netty.bytebuf.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-06-03 14:10
 */
public class HttpServer {

    private final static int DEFAULT_PORT = 8080;

    private int port;

    public HttpServer() {
        this(DEFAULT_PORT);
    }

    public HttpServer(int port) {
        this.port = port;
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(Short.MAX_VALUE))
                                    .addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
                                            FullHttpRequest request = msg.retain();
                                            byte[] bytes = new byte[request.content().readableBytes()];
                                            request.content().readBytes(bytes);
                                            System.out.println("接收Http客户端发送的消息：" + new String(bytes));

                                            ByteBuf content =
                                                    Unpooled.wrappedBuffer("我已接收到你的消息".getBytes(CharsetUtil.UTF_8));
                                            DefaultFullHttpResponse response =
                                                    new DefaultFullHttpResponse(request.protocolVersion(),
                                                            HttpResponseStatus.OK, content);

                                            HttpUtil.setContentLength(response, content.readableBytes());
                                            ctx.channel().writeAndFlush(response);
                                        }
                                    });
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Http Server start...");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpServer().start();
    }
}
