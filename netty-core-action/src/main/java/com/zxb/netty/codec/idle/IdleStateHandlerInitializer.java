package com.zxb.netty.codec.idle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * {@link IdleStateHandler} 空闲连接，心跳检测，
 * 当连接空闲时间太长时，将会触发一个 {@link IdleStateEvent} 事件。然后，你可以在你的 {@link ChannelInboundHandler}
 * 中重写 {@link ChannelInboundHandler#userEventTriggered(ChannelHandlerContext, Object)} 方法来处理该事件.
 * @author Mr.zxb
 * @date 2020-05-14
 **/
public class IdleStateHandlerInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                // IdleStateHandler 将在被触发时发送一个 IdleStateEvent 事件
                // 如果连接超过60s没有接收或发送任何的数据，那么 IdleStateEvent将会使用一个 IdleStateEvent事件来调用
                // fireUserEventTriggered()方法。
                new IdleStateHandler(0, 0, 60));
        // HeartbeatHandler 实现来 userEventTriggered()方法，如果这个方法检测到 IdleStateEvent 事件，它将会发送心跳消息，
        // 并且添加一个将在发送操作失败时关闭连接的 ChannelFutureListener
        pipeline.addLast(new HeartbeatHandler());
    }

    /**
     * 实现 {@link #userEventTriggered(ChannelHandlerContext, Object)} 方法以及发送心跳消息
     */
    private static final class HeartbeatHandler extends ChannelInboundHandlerAdapter {

        /**
         * 发送到远程节点的心跳消息
         */
        public static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8)
        );

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            } else {
                // 如果不是 IdleStateEvent 事件，所以将它传递给下一个 ChannelInboundHandler 处理
                ctx.fireUserEventTriggered(evt);
            }
        }
    }
}
