package com.zxb.netty.close.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.TimeUnit;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-06-01 13:51
 */
public class ShutdownHandler implements SignalHandler {

    private NettyShutdownGracefullyAsSignal server;

    public ShutdownHandler(NettyShutdownGracefullyAsSignal signal) {
        this.server = signal;
    }

    @Override
    public void handle(Signal signal) {
        // 信号量名称
        String name = signal.getName();
        // 信号量数值
        int number = signal.getNumber();

        System.out.println(name + " | " + number);
        System.out.println("执行 ShutdownHandler 的 handle 方法");
        System.out.println("ShutdownHook execute start...");
        System.out.print("Netty NioEventLoopGroup shutdownGracefully...");
        server.shutdown();
        System.out.println("ShutdownHook execute end...");
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//
//        }));
    }
}
