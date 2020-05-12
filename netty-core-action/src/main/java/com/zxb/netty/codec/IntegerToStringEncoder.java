package com.zxb.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 扩展 {@link MessageToMessageEncoder} 将 {@link Integer} 转 {@link String} 的编码器
 *
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class IntegerToStringEncoder extends MessageToMessageEncoder<Integer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        // 将 Integer 转换为 String，并添加到 list 中
        out.add(msg.toString());
    }
}
