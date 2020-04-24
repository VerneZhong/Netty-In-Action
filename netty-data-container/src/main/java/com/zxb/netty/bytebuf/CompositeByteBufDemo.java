package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 使用基于Netty {@link CompositeByteBuf} 的复合缓冲区模式
 *
 * @author Mr.zxb
 * @date 2020-04-24
 **/
public class CompositeByteBufDemo {

    /**
     * 使用 CompositeByteBuf
     */
    public void userCompositeByteBuf() {

        // CompositeByteBuf 可能不支持访问其支撑数组，因此访问 CompositeByteBuf中的数据类似于直接缓冲区的模式
        CompositeByteBuf messageBuf = Unpooled.compositeBuffer();

        // can be backing or direct
        ByteBuf header = Unpooled.directBuffer();
        ByteBuf body = Unpooled.directBuffer();

        // 将 ByteBuf 实例追加到 CompositeByteBuf
        messageBuf.addComponents(header, body);
        // ...
        // remove the header
        messageBuf.removeComponent(0);

        // 循环遍历所有的 ByteBuf 实例
        for (ByteBuf byteBuf : messageBuf) {
            System.out.println(byteBuf.toString());
        }
    }

    /**
     * 访问 CompositeByteBuf 中的数据
     */
    public void accessCompositeByteBufData() {
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        // 获取可读字节数
        int length = byteBuf.readableBytes();

        // 分配一个具有可读字节数组长度的新数组
        byte[] array = new byte[length];

        // 将字节读到该数组中
        byteBuf.getBytes(byteBuf.readerIndex(), array);

        // 使用偏移量和长度作为参数使用该数组
        handleArray(array, 0, length);
    }

    private void handleArray(byte[] array, int i, int length) {
    }
}
