package com.zxb.netty.channelhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * {@link ChannelHandlerContext} 的使用示例
 * @author Mr.zxb
 * @date 2020-04-29
 **/
public class ChannelHandlerContextDemo {

    public static void main(String[] args) {

        // 从 ChannelHandlerContext 访问 Channel
        ChannelHandlerContext channelHandlerContext = null;
        Channel channel = channelHandlerContext.channel();
        // 通过 Channel 写入缓冲区
        channel.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));

        // 通过 ChannelHandlerContext 访问 ChannelPipeline
        ChannelPipeline pipeline = channelHandlerContext.pipeline();
        // 通过 ChannelPipeline 写入缓冲区
        pipeline.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));

        // 调用 ChannelHandlerContext 的 write() 方法
        // write() 方法将把缓冲区数据发送到下一个 ChannelHandler
        channelHandlerContext.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8));
    }
}
