package com.zxb.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 扩展 {@link MessageToMessageEncoder} 的编码器，用于将负值整数转换为绝对值
 *
 * @author Mr.zxb
 * @date 2020-05-08
 **/
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        // 检查是否有足够的字节用来编码
        while (msg.readableBytes() >= 4) {
            // 从输入的 ByteBuf 中读取下一个整数，并且计算其绝对值
            int abs = Math.abs(msg.readInt());
            // 将该整数写入到编码消息的 list 中
            out.add(abs);
        }
    }
}
