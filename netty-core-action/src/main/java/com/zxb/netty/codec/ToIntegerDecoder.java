package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 扩展 {@link ByteToMessageDecoder} 将字节解码为特定的格式
 * <p>
 *     需求：接收一个包含简单 {@code int} 的字节流，每个 {@code int} 都需要被单独处理，每次在入站的 {@link ByteBuf} 读取4个字节，
 *     将其解码为一个 {@code int}，然后经过处理，添加到 {@code list} 中
 * </p>
 *
 * @author Mr.zxb
 * @date 2020-05-09
 **/
public class ToIntegerDecoder extends ByteToMessageDecoder {

    public static final int READABLE_BYTE = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 检查是否至少有4个字节可读（1个int的字节长度）
        // ReplayingDecoder 不需要验证所输入的 ByteBuf 是否具有足够的数据
        while (in.readableBytes() >= READABLE_BYTE) {
            // 从入站 ByteBuf 中读取一个 int， 并将其添加到解码消息到 list 中
            out.add(in.readInt() * 2);
        }
    }
}
