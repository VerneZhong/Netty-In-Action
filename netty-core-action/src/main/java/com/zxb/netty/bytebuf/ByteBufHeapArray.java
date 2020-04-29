package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBuf堆缓冲区，最常用的 {@link ByteBuf} 模式是将数据存储在JVM的堆空间中。这种模式称为支撑数组，
 * 它能在没有使用池化的情况下提供快速的分配和释放。
 *
 * @author Mr.zxb
 * @date 2020-04-24
 **/
public class ByteBufHeapArray {

    public static void main(String[] args) {

        ByteBuf heapBuf = Unpooled.buffer();

        // 检查 ByteBuf 是否有一个支撑数组
        if (heapBuf.hasArray()) {
            // 如果有，就获取该数组的引用
            byte[] array = heapBuf.array();

            // 计算第一个字节的偏移量
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            // 获取可读字节数
            int length = heapBuf.readableBytes();
            // 使用数组、偏移量和长度作为参数调用你的方法
            handleArray(array, offset, length);
        }

    }

    /**
     * 数据处理
     * @param array
     * @param offset
     * @param length
     */
    private static void handleArray(byte[] array, int offset, int length) {
    }
}
