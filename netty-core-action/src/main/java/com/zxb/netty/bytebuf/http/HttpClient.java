package com.zxb.netty.bytebuf.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Http 协议栈 {@link ByteBuf} 使用问题
 *
 * @author Mr.zxb
 * @date 2020-06-03 13:51
 */
public class HttpClient {

    private Channel channel;

    private HttpClientHandler handler = new HttpClientHandler();

    private void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(Short.MAX_VALUE))
                                    .addLast(handler);
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("Http Client start...");
            this.channel = future.channel();
            sendReq();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendReq() throws Exception {
        ByteBuf body = Unpooled.wrappedBuffer("Http message!".getBytes(CharsetUtil.UTF_8));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                HttpMethod.GET,
                "http://127.0.0.1/user?id=10&addr=BeiJing",
                body);
        HttpUtil.setContentLength(request, request.content().readableBytes());
        System.out.println("客户端发送消息...");
        blockSend(request);
    }

    private MyHttpResponse blockSend(FullHttpRequest request) throws Exception {
        HttpUtil.setContentLength(request, request.content().readableBytes());
        DefaultPromise<MyHttpResponse> respPromise = new DefaultPromise<>(channel.eventLoop());
        handler.setRespPromise(respPromise);
        channel.writeAndFlush(request);
        MyHttpResponse httpResponse = respPromise.get();
        if (httpResponse != null) {
            System.out.print("The Client received http response, the body is :" + new String(httpResponse.body()));
        }
        return httpResponse;
    }

    private class MyHttpResponse {

        private FullHttpResponse response;

        public MyHttpResponse(FullHttpResponse response) {
            this.response = response;
        }

        public byte[] body() {
            // 会出现 java.lang.UnsupportedOperationException: direct buffer
            // 因为 response的底层实现是直接内存 PooledUnsafeDirectByteBuf，不支持 array方法
//            return response.content() != null ? response.content().array() : null;

            // 需要将字节拷贝的方式将数据拷贝到byte数组中
            byte[] body = new byte[response.content().readableBytes()];
            response.content().readBytes(body);
            return body;
        }
    }

    private class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

        private DefaultPromise<MyHttpResponse> promise;

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
            promise.setSuccess(new MyHttpResponse(msg.retain()));
        }

        public void setRespPromise(DefaultPromise<MyHttpResponse> respPromise) {
            this.promise = respPromise;
        }
    }

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        client.connect("127.0.0.1", 8080);
    }
}
