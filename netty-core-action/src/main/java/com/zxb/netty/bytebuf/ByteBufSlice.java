package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 派生缓冲区为 {@link ByteBuf} 提供以专门的方式来呈现其内容的视图。
 * 视图创建的方式：
 *  <ul>
 *      <li>{@link ByteBuf#duplicate()}</li>
 *      <li>{@link ByteBuf#slice()}</li>
 *      <li>{@link ByteBuf#slice(int, int)}</li>
 *      <li>{@link ByteBuf#asReadOnly()}</li>
 *      <li>{@link ByteBuf#readSlice(int)}</li>
 *  </ul>
 *  这些方法都将返回一个新的 {@link ByteBuf} 实例，它具有自己的读索引、写索引和标记索引。其内部存储和JDK 的 {@link ByteBuffer} 一样是共享的，
 *  使得派生缓冲区的创建成本很低廉，也意味着，如果修改了它的内容，也同时修改了其对应的源实例。
 * <p>
 * 对 {@link ByteBuf} 进行切片示例
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufSlice {

    public static void main(String[] args) {

        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        // 返回该ByteBuf从下标0到15的新切片
        ByteBuf slice = byteBuf.slice(0, 15);
        System.out.println(slice.toString(utf8));

        // 更新切片 0索引处的字节
        slice.setByte(0, 'J');
        // 返回true，因为数据是共享的，对其中一个所做对更改对另外一个也是可见
        System.out.println(byteBuf.getByte(0) == slice.getByte(0));
        System.out.println(byteBuf.toString(utf8));
        System.out.println(slice.toString(utf8));
    }
}
