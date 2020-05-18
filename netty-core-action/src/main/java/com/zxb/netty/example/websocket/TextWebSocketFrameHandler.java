package com.zxb.netty.example.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 扩展 {@link SimpleChannelInboundHandler} 处理 WebSocket {@link TextWebSocketFrame} 文本帧
 *
 * @author Mr.zxb
 * @date 2020-05-18
 **/
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup channelGroup;

    public TextWebSocketFrameHandler(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }

    /**
     * 重写 userEventTriggered 方法以处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
//        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手成功则移除 HttpRequestHandler，不在处理 Http 请求消息
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已连接的 WebSocket 客户端新的客户端已经连接上了
            channelGroup.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + "joined!"));
            // 将新的 WebSocket Channel 添加到 ChannelGroup 中，以便它可以接收到所有的消息
            channelGroup.add(ctx.channel());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 增加消息的引用计数，防止被释放掉引用，并将它写到 ChannelGroup 中所有已经连接的客户端
        channelGroup.writeAndFlush(msg.retain());
    }
}
