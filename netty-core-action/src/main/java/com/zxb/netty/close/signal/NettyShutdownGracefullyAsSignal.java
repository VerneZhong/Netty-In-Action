package com.zxb.netty.close.signal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import sun.misc.Signal;

/**
 * 除了注册 {@code SignalHandler} 监听信号量并注册 {@link sun.misc.SignalHandler} 的方式来实现优雅退出
 *
 * @author Mr.zxb
 * @date 2020-06-01 12:06
 */
public class NettyShutdownGracefullyAsSignal {

    private NioEventLoopGroup eventLoopGroup;

    public NettyShutdownGracefullyAsSignal() {
        this.eventLoopGroup = new NioEventLoopGroup();
    }

    public void start() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(8080)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {

                        }
                    });
            serverBootstrap.bind().sync();
            System.out.println("Netty Server start...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        eventLoopGroup.shutdownGracefully();
    }

    private static String getOSSignalType() {
        return System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "TERM";
    }

    public static void main(String[] args) {
        NettyShutdownGracefullyAsSignal server = new NettyShutdownGracefullyAsSignal();
        Signal signal = new Signal(getOSSignalType());

        // 注册对指定信号的处理
        // kill or kill -15
//        Signal.handle(new Signal("TERM") ,new ShutdownHandler(server));
        Signal.handle(new Signal("INT"), new ShutdownHandler(server));

//        Signal.handle(signal, new ShutdownHandler(server));
        server.start();
    }
}
