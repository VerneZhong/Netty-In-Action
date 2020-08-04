package com.zxb.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 路由转发服务，该示例不会导致内存泄漏，因为父类替我们释放了资源，我们只关心业务逻辑即可
 *
 * @author Mr.zxb
 * @date 2020-06-03 10:32
 */
public class RouterServerHandlerV2 extends SimpleChannelInboundHandler<ByteBuf> {

    static ExecutorService executorService = Executors.newSingleThreadExecutor();

    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
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
    }
}
