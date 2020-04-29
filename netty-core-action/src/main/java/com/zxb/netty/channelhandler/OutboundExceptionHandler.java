package com.zxb.netty.channelhandler;

import io.netty.channel.*;

/**
 * {@link ChannelOutboundHandlerAdapter} 用于处理出站操作中的正常完成以及异常的选项，都基于以下通知机制
 * <p>
 *     1. 每个出站操作都将返回一个 {@link ChannelFuture}，注册到 {@link ChannelFuture} 的 {@link ChannelFutureListener} 将在操作完成时被通知。
 * </p>
 * <p>
 *     2. 几乎所有的 {@link ChannelOutboundHandler} 上的方法都会传入一个 {@link ChannelPromise} 的实例，被分配用于异步通知的监视器。
 *        {@link ChannelPromise#setSuccess()} 和 {@link ChannelPromise#setFailure(Throwable)} 提供立即通知单方法。
 * </p>
 * @author Mr.zxb
 * @date 2020-04-29
 **/
public class OutboundExceptionHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 方式一：添加 ChannelFutureListener 到 ChannelFuture
        ChannelFuture channelFuture = ctx.channel().write(msg);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
                future.channel().close();
            }
        });

        // 方式二：添加 ChannelFutureListener 到 ChannelPromise
        promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                future.cause().printStackTrace();
                future.channel().close();
            }
        });
    }
}
