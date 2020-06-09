package com.zxb.netty.apigateway;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 高并发下API Gateway 性能低下
 *
 * @author Mr.zxb
 * @date 2020-06-08 17:02
 */
public class ApiGatewayServerHandler extends ChannelInboundHandlerAdapter {

    private ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 该方式每次都会创建一个 64kb 大小 char 数组，高并发下容易造成频繁 GC，降低吞吐量
//        char[] req = new char[64 * 1024];
        // 按照请求消息来初始化 char 数组
        char[] req = new char[((ByteBuf) msg).readableBytes()];
        executorService.execute(() -> {
            char[] dispatchReq = req;
            // 简单处理之后，转发请求消息到后端服务
            // 省略代码。。。

            try {
                // 模拟业务逻辑处理耗时 0.5s
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
