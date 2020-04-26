package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBuf} 字节级操作，随机索引访问数据
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufRandomAccess {

    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello ByteBuf for Random Access Data", StandardCharsets.UTF_8);

        // ByteBuf 的索引是从0开始，最后一个字节的索引是 capacity() - 1
        for (int i = 0; i < byteBuf.capacity(); i++) {
            byte bufByte = byteBuf.getByte(i);
            System.out.print((char) bufByte);
        }
    }
}
