package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

/**
 * ByteBuf直接缓冲区，是 {@link ByteBuf} 的另一种模式。在Java NIO中的 {@link ByteBuffer} 类允许JVM实现通过本地调用来分配内存。
 * 这主要是为了避免在每次调用本地I/O操作之前或之后将缓冲区的内容复制到一个中间缓冲区。
 * 直接缓冲区的内容将驻留在常规的会被垃圾回收的堆之外。
 * 直接缓冲区的缺点：相对于基于堆的缓冲区，分配和释放比较昂贵
 *
 * @author Mr.zxb
 * @date 2020-04-24
 **/
public class ByteBufDirectArray {

    public static void main(String[] args) {

        ByteBuf directBuf = Unpooled.directBuffer();

        // 检查 ByteBuf 是否由数组支撑，如果不是，则这是一个直接缓冲区
        if (!directBuf.hasArray()) {
            // 获取可读字节数
            int length = directBuf.readableBytes();
            // 分配一个新的数组来保存具有该长度的字节数据
            byte[] array = new byte[length];
            // 将字节复制到该数组
            directBuf.getBytes(directBuf.readerIndex(), array);
            // 使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array, 0, length);
        }
    }

    private static void handleArray(byte[] array, int i, int length) {
    }
}
