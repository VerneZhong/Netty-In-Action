package com.zxb.netty.codec.websocket.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import java.util.Locale;

/**
 * 基于 Netty 构建 {@link WebSocketServerProtocolHandler} 的 WebSocket 服务端应用程序. <br/>
 * 要想 WebSocket 支持 ssl，则需要将 {@link SslHandler} 作为第一个 {@link ChannelHandler} 添加到 {@link ChannelPipeline} 中.
 *
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class WebSocketServerInitializer extends ChannelInitializer<Channel> {

    private static final String WEBSOCKET_PATH = "/websocket";

    private final SslContext sslContext;

    public WebSocketServerInitializer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (sslContext != null) {
            pipeline.addFirst(sslContext.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpServerCodec(),
                // 为握手提供聚合的 HttpRequest
                new HttpObjectAggregator(65356),
                // 用于压缩 WebSocket 服务端传输
                new WebSocketServerCompressionHandler(),
                // 如果被请求的端点是 "/websocket" 则处理该升级握手
                new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true),
                // 用于处理浏览器的显示页面
                new WebSocketIndexPageHandler(WEBSOCKET_PATH),
                // 用于处理 TextWebSocketFrame
                new TextFrameHandler(),
                // 用于处理 BinaryWebSocketFrame
                new BinaryFrameHandler(),
                // 用于处理 ContinuationWebSocketFrame
                new ContinuationFrameHandler());
    }

    public static final class TextFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
            String request = msg.text();
            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        }
    }

    public static final class BinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
            // do something
        }
    }

    public static final class ContinuationFrameHandler extends SimpleChannelInboundHandler<ContinuationWebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ContinuationWebSocketFrame msg) throws Exception {
            // do something
        }
    }
}
