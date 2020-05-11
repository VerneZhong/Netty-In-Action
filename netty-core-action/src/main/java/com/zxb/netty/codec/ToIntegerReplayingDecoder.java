package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 扩展 {@link ReplayingDecoder} 将字节解码为特定的格式
 * <p>
 *     <ul>
 *         <li>ReplayingDecoderByteBuf 如果所用的 {@link ByteBuf} 不被支持，将会抛出 {@link UnsupportedOperationException} 异常</li>
 *         <li>{@link ReplayingDecoder} 稍慢于 {@link ByteToMessageDecoder}</li>
 *     </ul>
 * </p>
 * @author Mr.zxb
 * @date 2020-05-11
 **/
public class ToIntegerReplayingDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 传入的 ByteBuf 是 ReplayingDecoderByteBuf
        // 如果没有足够的字节时，readInt()方法的实现将会抛出一个 Signal 错误，并被捕获并处理
        // 从入站 ByteBuf 中读取一个 int，并将其添加到解码消息到 list 中
        out.add(in.readInt() * 2);
    }
}
