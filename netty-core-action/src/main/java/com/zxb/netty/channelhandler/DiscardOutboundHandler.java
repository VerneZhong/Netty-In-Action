package com.zxb.netty.channelhandler;

import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.ReferenceCountUtil;

/**
 * 扩展自 {@link ChannelOutboundHandlerAdapter} 释放出站消息资源。
 * <p>
 *     总之，如果一个消息被消费或者丢弃，并且没有传递给 {@link ChannelPipeline} 中的下一个 {@link ChannelOutboundHandler}，
 *     那么用户就必须调用 {@link ReferenceCountUtil#release(Object)} 方法显式释放资源。
 *     如果消息到达了实际的传输层，那么当它被写入时或者 {@link Channel} 关闭时，都将被自动释放。
 * </p>
 * @author Mr.zxb
 * @date 2020-04-28
 **/
@Sharable
public class DiscardOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 显式释放资源
        ReferenceCountUtil.release(msg);
        // 通知 ChannelPromise 消息已经被处理了
        promise.setSuccess();
    }
}
