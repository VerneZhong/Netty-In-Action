package com.zxb.netty.channelhandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 可共享的 {@link ChannelHandler}
 * @author Mr.zxb
 * @date 2020-04-29
 **/
@Sharable
public class SharableHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Channel Read message = " + msg);
        // 记录方法调用，转发给下一个 ChannelHandler 处理
        ctx.fireChannelRead(msg);
    }
}
