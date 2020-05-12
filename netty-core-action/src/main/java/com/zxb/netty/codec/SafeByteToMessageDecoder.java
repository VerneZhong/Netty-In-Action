package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 带有默认帧长度的 {@link ByteToMessageDecoder} 解码器，当超出长度将抛出 {@link TooLongFrameException} 异常，
 * 防止帧大小溢出
 *
 * @author Mr.zxb
 * @date 2020-05-11
 **/
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {

    /**
     * 默认最大的帧长度
     */
    public static final int DEFAULT_MAX_FRAME_SIZE = 1024;

    /**
     * 自定义的帧长度
     */
    private final int frameSize;

    public SafeByteToMessageDecoder() {
        this(DEFAULT_MAX_FRAME_SIZE);
    }

    public SafeByteToMessageDecoder(int frameSize) {
        this.frameSize = frameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();
        // 检查缓冲区中是否有超过 frameSize个字节
        if (readableBytes > frameSize) {
            // 跳过所有的可读字节，抛出 TooLongFrameException，并通知 ChannelHandler
            in.skipBytes(readableBytes);
            throw new TooLongFrameException();
        }

        // 添加可读数据
        out.add(in.readBytes(readableBytes));
    }
}
