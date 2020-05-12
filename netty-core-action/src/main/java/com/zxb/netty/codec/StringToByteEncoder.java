package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 扩展于 {@link MessageToByteEncoder} 的 {@link String} 转 {@link Byte} 编码器
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class StringToByteEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        // 将 String 写入 ByteBuf 中
        out.writeBytes(msg.getBytes());
    }
}
