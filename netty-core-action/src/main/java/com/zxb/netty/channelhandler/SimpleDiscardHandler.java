package com.zxb.netty.channelhandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 无需显式的释放资源，{@link SimpleChannelInboundHandler#channelRead(ChannelHandlerContext, Object)} 自动调用了资源释放的方法
 * @author Mr.zxb
 * @date 2020-04-27
 **/
@Sharable
public class SimpleDiscardHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 不需要任何显式的资源释放
        // 其他业务逻辑
    }
}
