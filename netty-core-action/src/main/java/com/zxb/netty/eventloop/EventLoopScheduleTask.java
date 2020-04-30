package com.zxb.netty.eventloop;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于Netty {@link EventLoop} 调度任务
 * @author Mr.zxb
 * @date 2020-04-30
 **/
public class EventLoopScheduleTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建 Channel
        Channel channel = new NioSocketChannel();

        // 获取 Channel 的 EventLoop 执行调度任务
        ScheduledFuture<?> future = channel.eventLoop().schedule(() -> System.out.println("60 seconds later"),
                // 任务在创建之后的60s后执行
                60, TimeUnit.SECONDS);

        // 等待任务执行
        future.get();

        // 关闭 Channel
        channel.close();
    }
}
