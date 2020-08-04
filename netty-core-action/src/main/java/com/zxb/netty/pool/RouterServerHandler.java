package com.zxb.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 路由转发服务
 * <p>该示例会导致进程内存不断飙升，最终导致内存泄漏问题，主要原因是没有释放 msg 资源</p>
 * @author Mr.zxb
 * @date 2020-06-03 10:23
 */
public class RouterServerHandler extends ChannelInboundHandlerAdapter {

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf reqMsg = (ByteBuf) msg;
        byte[] body = new byte[reqMsg.readableBytes()];
        reqMsg.readBytes(body);
        executorService.execute(() -> {
            // 解析请求信息，做路由转发
            // 省略转发...
            // 转发成功，返回响应给客户端
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            respMsg.writeBytes(body);
            ctx.writeAndFlush(respMsg);
        });

        // 释放内存，解决内存泄漏问题
        ReferenceCountUtil.release(reqMsg);
    }
}
