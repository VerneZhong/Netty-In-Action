package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 扩展 {@link MessageToByteEncoder} 编码器
 *
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class CharacterToByteEncoder extends MessageToByteEncoder<Character> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
        // 将 Character 解码为 char，并将其写入到 ByteBuf 中
        out.writeChar(msg);
    }
}
