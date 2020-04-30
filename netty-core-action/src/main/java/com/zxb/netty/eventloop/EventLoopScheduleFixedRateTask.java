package com.zxb.netty.eventloop;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 基于Netty {@link EventLoop} 周期性调度任务
 * @author Mr.zxb
 * @date 2020-04-30
 **/
public class EventLoopScheduleFixedRateTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建 NIO EventLoopGroup 实例
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        // 创建 Channel
        Channel channel = new NioSocketChannel();

        // 注册 Channel
        eventLoopGroup.register(channel);

        // 获取 Channel 的 EventLoop 执行周期性的调度任务
        ScheduledFuture<?> future = channel.eventLoop().scheduleAtFixedRate(() -> System.out.println("60 seconds later"),
                // 任务在创建之后的每隔60s执行一次
                60, 60, TimeUnit.SECONDS);

        // 等待任务执行
        future.get();

        // 关闭 Channel
        channel.close();
    }
}
