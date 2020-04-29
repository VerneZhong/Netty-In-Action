package com.zxb.netty.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 扩展自 {@link ChannelInboundHandlerAdapter} 消费并释放入站消息资源
 * @author Mr.zxb
 * @date 2020-04-27
 **/
@Sharable
public class DiscardInboundHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当重写 {@link ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)} 方法时，
     * 它将负责显式地释放与池化的 {@link ByteBuf} 实例相关的内存
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 释放已接收的消息
        ReferenceCountUtil.release(msg);
    }

}
