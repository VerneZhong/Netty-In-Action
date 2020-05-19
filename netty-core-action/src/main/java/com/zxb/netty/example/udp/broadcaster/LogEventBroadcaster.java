package com.zxb.netty.example.udp.broadcaster;

import com.zxb.netty.example.udp.codec.LogEventEncoder;
import com.zxb.netty.example.udp.pojo.LogEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.zxb
 * @date 2020-05-18 22:07:58
 */
public class LogEventBroadcaster {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;
    private final int port = 9999;
    private final String host = "255.255.255.255";

    public LogEventBroadcaster(File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.file = file;
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(new InetSocketAddress(host, port)));
    }

    public void run() throws Exception {
        Channel channel = bootstrap.bind(0).sync().channel();
        System.out.println("LogEventBroadcaster running...");
        long pointer = 0;
        for (; ; ) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        // log 文件路径
        File file = new File("/Users/zhongxuebin/log/sengled/life2-2020-05-18.log");
        LogEventBroadcaster logEventBroadcaster = new LogEventBroadcaster(file);

        try {
            logEventBroadcaster.run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logEventBroadcaster.stop();
        }
    }
}
