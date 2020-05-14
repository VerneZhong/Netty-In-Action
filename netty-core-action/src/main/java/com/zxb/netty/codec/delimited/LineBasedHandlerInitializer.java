package com.zxb.netty.codec.delimited;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

/**
 * 处理由行尾符分隔的帧 <br/>
 * {@link DelimiterBasedFrameDecoder}：使用任何用户提供的分隔符来提取帧的通用解码器 <br/>
 * {@link LineBasedFrameDecoder}：提取由行尾符（\n或者\r\n）分隔的帧的解码器。这个解码器比 {@link DelimiterBasedFrameDecoder} 的分隔符解码器要快
 *
 * @author Mr.zxb
 * @date 2020-05-14
 **/
public class LineBasedHandlerInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 添加 64 * 1024 最大长度的 LineBasedFrameDecoder ，它会将提取的帧转发给下一个 ChannelInboundHandler
                .addLast(new LineBasedFrameDecoder(64 * 1024))
                // 添加 FrameHandler 以接收帧
                .addLast(new FrameHandler());
    }

    private class FrameHandler extends SimpleChannelInboundHandler<ByteBuf> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//             输出 接收的帧内容
            System.out.println(msg.toString(CharsetUtil.UTF_8));
        }
    }
}
