package com.zxb.netty.channelhandler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link Sharable} 的错误用法，只有确保 {@link ChannelHandler} 是线程安全时才使用 {@link Sharable}
 * @author Mr.zxb
 * @date 2020-04-29
 **/
@Sharable
public class UnSharableHandler extends ChannelInboundHandlerAdapter {

    /**
     * 将变量修改为 线程安全的原子整数类即可
     */
    private int count;
    private AtomicInteger atomicCount;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 记录count变量 + 1
        count++;
//         自增 1
//        atomicCount.getAndIncrement();
        System.out.println("channelRead(...) called the " + count + " time");
        // 转发给下一个 ChannelHandler
        ctx.fireChannelRead(msg);
    }
}
