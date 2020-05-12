package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 扩展 {@link ByteToMessageDecoder} 解码器
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class ByteToCharacterDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 每次从 ByteBuf 中提取 2个字节，作为 char 添加到 list 中
        while (in.readableBytes() >= 2) {
            // 将一个或者多个 Character 对象添加到传出到 List 中
            out.add(in.readChar());
        }
    }
}
