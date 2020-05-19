package com.zxb.netty.example.udp.monitor;

import com.zxb.netty.example.udp.codec.LogEventDecoder;
import com.zxb.netty.example.udp.codec.LogEventEncoder;
import com.zxb.netty.example.udp.handler.LogEventHandler;
import com.zxb.netty.example.udp.pojo.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * {@link LogEvent} 监视者，接收广播的消息
 *
 * @author Mr.zxb
 * @date 2020-05-19
 **/
public class LogEventMonitor {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;

    public LogEventMonitor(InetSocketAddress address) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(group)
                // 引导该 NioDatagramChannel
                .channel(NioDatagramChannel.class)
                // 设置套接字选项 SO_BROADCAST
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                // 将 LogEventDecoder 和 LogEventHandler 添加到 ChannelPipeline
                                .addLast(new LogEventDecoder())
                                .addLast(new LogEventHandler());
                    }
                }).localAddress(address);
    }

    public Channel bind() {
        // 绑定 Channel, 注意 DatagramChannel 是无连接的
        return bootstrap.bind().syncUninterruptibly().channel();
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws InterruptedException {
        // 构造一个新的 LogEventMonitor
        LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(9999));
        try {
            Channel channel = monitor.bind();
            System.out.println("LogEventMonitor running...");
            channel.closeFuture().sync();
        } finally {
            monitor.stop();
        }
    }
}
