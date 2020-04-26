package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBuf} 读取所有数据
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufReadAllData {

    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.copiedBuffer("Hi zxb!", Charset.forName("GB2312"));

        // 循环读取数据
        while (byteBuf.isReadable()) {
            System.out.print((char) byteBuf.readByte());
        }
    }
}
