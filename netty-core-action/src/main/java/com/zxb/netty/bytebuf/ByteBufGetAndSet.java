package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBuf} get...() 和 set...() 操作
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufGetAndSet {

    public static void main(String[] args) {

        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        // 输出第一个字符
        System.out.println((char) byteBuf.getByte(0));

        // 存储当前 读、写索引
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();

        // 更新索引0的字符
        byteBuf.setByte(0, 'B');
        System.out.println((char) byteBuf.getByte(0));

        // 返回true，因为这些操作不会修改相应的读写索引
        System.out.println(readerIndex == byteBuf.readerIndex());
        System.out.println(writerIndex == byteBuf.writerIndex());
    }
}
