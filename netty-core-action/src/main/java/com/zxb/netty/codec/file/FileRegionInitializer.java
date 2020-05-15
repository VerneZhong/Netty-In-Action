package com.zxb.netty.codec.file;

import io.netty.channel.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 写大型数据：为了避免写大文件时，耗尽内存的风险，在写大型数据时，需要准备好处理到远程节点的连接是慢速连接的情况，
 * 这种情况会导致内存释放的延迟。
 * NIO的零拷贝的特性，这种特性消除了将文件的内容从文件系统移动到网络栈的复制过程。
 * Netty 使用 {@link FileRegion} 接口的实现来支持零拷贝的文件传输的 {@link Channel} 来发送的文件区域，
 * 该示例只适用于文件内容的直接传输，不包括应用程序对数据的任何处理。在需要将数据从文件系统复制到用户内存中时，
 * 可以使用 {@link ChunkedWriteHandler}，它支持异步写大型数据流，而不会导致大量的内存消耗
 *
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class FileRegionInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new SimpleChannelInboundHandler<File>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, File file) throws Exception {
                super.channelActive(ctx);
                // 创建一个 FileInputStream
                FileInputStream in = new FileInputStream(file);
                // 以该文件的完整长度创建一个新的 DefaultFileRegion
                FileRegion fileRegion = new DefaultFileRegion(in.getChannel(), 0, file.length());

                // 发送该 DefaultFileRegion，并注册一个 ChannelFutureListener，完成并通知
                // ChannelProgressivePromise 监听实时获取传输的进度
                ctx.writeAndFlush(fileRegion).addListener((ChannelFutureListener) future -> {
                    // 失败时处理
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                    } else {
                        System.out.println("传输完成.");
                    }
                });
            }
        });
    }

}
