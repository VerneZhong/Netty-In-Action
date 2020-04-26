package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.nio.charset.StandardCharsets;

/**
 * {@link ByteProcessor#process(byte)} 查找 {@link ByteBuf} 数据操作
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteProcessorFindData {

    public static void main(String[] args) {

        ByteBuf buf = Unpooled.copiedBuffer("Hi zxb!\r", StandardCharsets.UTF_8);

        // 查找 \r
        int i1 = buf.forEachByte(ByteProcessor.FIND_CR);
        System.out.println("\\r所在的下标：" + i1);

        // 查找字符 "z"
        int index = buf.forEachByte(new ByteProcessor.IndexOfProcessor("z".getBytes()[0]));
        System.out.println("\"z\"所在的下标：" + index);

        // 查找空格
        int i = buf.forEachByte(ByteProcessor.FIND_LINEAR_WHITESPACE);
        System.out.println("空格所在的下标：" + i);

    }
}
