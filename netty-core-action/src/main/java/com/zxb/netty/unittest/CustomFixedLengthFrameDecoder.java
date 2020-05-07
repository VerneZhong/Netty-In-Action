package com.zxb.netty.unittest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 扩展 {@link ByteToMessageDecoder} 以处理入站字节，并将它们解码为消息
 * @author Mr.zxb
 * @date 2020-05-07 21:38:06
 */
public class CustomFixedLengthFrameDecoder extends ByteToMessageDecoder {

    /**
     * 指定要生成的帧的长度
     */
    private final int frameLength;

    public CustomFixedLengthFrameDecoder(int frameLength) {
        if (frameLength < 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 检查是否有足够的字节可以被读取，以生成下一个帧
        while (byteBuf.readableBytes() >= frameLength) {
            // 从 ByteBuf 中读取一个新帧
            ByteBuf readBytes = byteBuf.readBytes(frameLength);
            // 将该帧添加到已被解码的消息列表中
            list.add(readBytes);
        }
    }
}
