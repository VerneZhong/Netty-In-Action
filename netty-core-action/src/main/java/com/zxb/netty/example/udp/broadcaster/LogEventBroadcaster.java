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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * {@link LogEvent} 广播者，将消息广播出去
 * @author Mr.zxb
 * @date 2020-05-18 22:07:58
 */
public class LogEventBroadcaster {

    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;
    private final int port = 9999;
    /**
     * 受限的广播地址或者零网络地址 <a href="https://en.wikipedia.org/wiki/Broadcast_address">255.255.255.255</a>
     * 把每一行作为一个消息广播到一个指定的端口，发送到这个地址的消息都将会被定向给本地网络（0.0.0.0）上的所有主机，而不会被路由器转发给其他的网络
     */
    private final String host = "255.255.255.255";

    public LogEventBroadcaster(File file) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.file = file;
        bootstrap.group(group)
                // 引导该 NioDatagramChannel（无连接的）
                .channel(NioDatagramChannel.class)
                // 设置 SO_BROADCAST 套接字选项
                .option(ChannelOption.SO_BROADCAST, true)
                .handler(new LogEventEncoder(new InetSocketAddress(host, port)));
    }

    public void run() throws Exception {
        //绑定 Channel，UDP 协议的连接用 bind() 方法
        Channel channel = bootstrap.bind(0).sync().channel();
        System.out.println("LogEventBroadcaster running...");

        // 启动主处理循环
        readFileData(channel);
    }

    private void readFileData(Channel channel) throws IOException, InterruptedException {
        long pointer = 0;
        // 启动主处理循环
        for (; ; ) {
            long len = file.length();
            if (len < pointer) {
                // file was reset
                // 如果有必要，将文件指针设置到该文件的最后一个字节
                pointer = len;
            } else if (len > pointer) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                // 设置当前的文件指针，以确保没有任何的旧日志被发送
                raf.seek(pointer);
                String line;
                while ((line = raf.readLine()) != null) {
                    // 对于每个日志条目，写入一个 LogEvent 到 Channel 中
                    channel.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                // 存储其在文件中的当前位置
                pointer = raf.getFilePointer();
                raf.close();
            }
            // 休眠1s，如果被中断，则退出循环；否则重新处理它
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        // log 文件路径
        File file = new File("/Users/zhongxuebin/log/sengled/mylog.log");
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
