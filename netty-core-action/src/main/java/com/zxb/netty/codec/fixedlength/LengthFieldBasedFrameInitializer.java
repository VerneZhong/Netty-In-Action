package com.zxb.netty.codec.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 解码器基于长度的协议 <br/>
 * {@link FixedLengthFrameDecoder}：提取在调用指定长度的帧信息的解码器 <br/>
 * {@link LengthFieldBasedFrameDecoder}：根据编码进帧头部的长度值提取帧；该字段的偏移量以及长度在构造函数中指定
 * @author Mr.zxb
 * @date 2020-05-14
 **/
public class LengthFieldBasedFrameInitializer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 使用 LengthFieldBasedFrameDecoder 解码将帧长度编码到帧起始的前8个字节中的消息
                .addLast(new LengthFieldBasedFrameDecoder(64 * 1024, 0, 8))
                .addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                        // Do something with the frame

                    }
                });
    }
}
