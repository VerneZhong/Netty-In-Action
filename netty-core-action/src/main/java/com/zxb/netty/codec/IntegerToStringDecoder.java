package com.zxb.netty.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 扩展 {@link MessageToMessageDecoder} 解码器，将 {@link Integer} 转化成 {@link String}
 * @author Mr.zxb
 * @date 2020-05-11
 **/
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
