package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBuf} read...() 和 write...() 操作
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufWriteAndRead {

    public static void main(String[] args) {

        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        // 输出第一个字符
        System.out.println((char) byteBuf.readByte());

        // 存储当前 读、写索引
        int readerIndex = byteBuf.readerIndex();
        int writerIndex = byteBuf.writerIndex();

        // 将字符追加到缓冲区
        byteBuf.writeByte('J');

        // 返回true，read 没有更新索引
        System.out.println(readerIndex == byteBuf.readerIndex());
        // 返回false，write 更新了索引
        System.out.println(writerIndex == byteBuf.writerIndex());
    }
}
