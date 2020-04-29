package com.zxb.netty.channelhandler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 缓存 {@link ChannelHandlerContext} 的引用
 * @author Mr.zxb
 * @date 2020-04-29
 **/
public class ChannelHandlerContextCache extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 存储 ChannelHandlerContext 的引用
        this.ctx = ctx;
    }

    /**
     * 使用缓存的 {@link ChannelHandlerContext} 引用来发送消息
     * @param msg
     */
    public void send(String msg) {
        ctx.writeAndFlush(msg);
    }
}
