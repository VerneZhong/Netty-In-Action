package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBuf#copy()} 复制：返回缓冲区的真实副本，不同于派生缓冲区，该复制的结果是独立的数据副本
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufCopy {

    public static void main(String[] args) {

        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hi Netty!", utf8);

        ByteBuf newBuf = byteBuf.copy();

        // 修改独立的新副本的内容，不会影响源副本
        newBuf.setByte(3, 'J');

        System.out.println("源ByteBuf: " + byteBuf.toString(utf8));
        System.out.println("新ByteBuf: " + newBuf.toString(utf8));
    }
}
