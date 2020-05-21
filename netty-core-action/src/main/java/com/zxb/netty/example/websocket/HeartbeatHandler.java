package com.zxb.netty.example.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * 心跳事件 Handler 处理
 *
 * @author Mr.zxb
 * @date 2020-05-21
 **/
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private final ChannelGroup group;

    public HeartbeatHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 发送到远程节点的心跳消息
     */
    public static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
            Unpooled.copiedBuffer("heartbeat", CharsetUtil.UTF_8)
    );

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            group.writeAndFlush(new TextWebSocketFrame(HEARTBEAT_SEQUENCE.duplicate()));
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }
}
