package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Random;

/**
 * {@link ByteBuf} 写数据
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufWriteData {

    public static void main(String[] args) {

        ByteBuf byteBuf = Unpooled.buffer();

        Random random = new Random();

        // 填充数据
        while (byteBuf.writableBytes() >= 1) {
            byteBuf.writeInt(random.nextInt());
        }

        // 读取数据
        while (byteBuf.isReadable()) {
            System.out.println(byteBuf.readByte());
        }
    }
}
