package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 按需分配 {@link ByteBufAllocator} 接口示例
 * <p>
 * 为了降低分配和释放内存的开销，Netty通过 {@link ByteBufAllocator} 实现了 {@link ByteBuf} 池化，
 * 它可以用来分配我们所描述过的任意类型的 {@link ByteBuf} 实例
 * <p>
 *     <ul>
 *         <li>{@link ByteBufAllocator#buffer()}：返回一个基于堆或者直接内存存储的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#heapBuffer()}：返回一个基于堆内存存储的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#directBuffer()}：返回一个基于直接内存存储的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#compositeBuffer()}：返回一个基于堆或直接内存存储的复合缓冲区的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#compositeHeapBuffer()}：返回一个基于堆内存存储的复合缓冲区的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#compositeDirectBuffer()}：返回一个基于直接内存存储的复合缓冲区的 {@link ByteBuf}</li>
 *         <li>{@link ByteBufAllocator#ioBuffer()}：返回一个用于套接字的I/O操作的 {@link ByteBuf}</li>
 *     </ul>
 * </p>
 * @author Mr.zxb
 * @date 2020-04-27
 **/
public class ByteBufAllocatorDemo {

    public static void main(String[] args) {

        // 使用 Channel 创建 ByteBufAllocator
        Channel channel = new NioSocketChannel();

        ByteBufAllocator byteBufAllocator = channel.alloc();

        ByteBuf buffer = byteBufAllocator.buffer();

        // 使用 ChannelHandlerContext 创建 ByteBufAllocator
        ChannelHandlerContext channelHandlerContext = null;

        ByteBufAllocator alloc = channelHandlerContext.alloc();

        ByteBuf heapBuffer = alloc.heapBuffer();

        // ByteBuf 和 ByteBufHolder 都用了引用计数，只要引用计数大于0，就能保证对象不会被释放
        // 引用计数
        ByteBuf byteBuf = byteBufAllocator.directBuffer();

        // 检查引用计数是否为预期的1
        assert byteBuf.refCnt() == 1;

        // 释放引用计数的对象
        // 减少该对象的活动引用。当减少到0时，该对象被释放，并且返回true
        boolean release = byteBuf.release();
    }
}
