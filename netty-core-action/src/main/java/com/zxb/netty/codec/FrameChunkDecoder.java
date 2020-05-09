package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * 扩展 {@link ByteToMessageDecoder} 以将入站字节解码为消息
 * @author Mr.zxb
 * @date 2020-05-08
 **/
public class FrameChunkDecoder extends ByteToMessageDecoder {

    /**
     * 指定将要产生的帧的最大允许大小
     */
    private final int maxFrameSize;

    public FrameChunkDecoder(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readableBytes = in.readableBytes();

        // 如果该帧太大，则丢弃它并抛出一个 TooLongFrameException
        if (readableBytes > maxFrameSize) {

            // discard the bytes
            in.clear();
            throw new TooLongFrameException();
        }

        // 从 ByteBuf 中读取一个新的帧
        ByteBuf byteBuf = in.readBytes(readableBytes);
        // 将该帧添加到解码消息的list中
        out.add(byteBuf);
    }
}
