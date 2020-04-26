package com.zxb.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * {@link ByteBufHolder} 接口：当我们需要存储各种属性值的时候该接口提供了缓冲区池化、其中可以从池中借用 {@link ByteBuf} ，
 * 并且子需要时自动释放等高级特性的支持
 * <ul>
 *     <li>{@link ByteBufHolder#content()}：返回该 {@link ByteBufHolder} 所持有的 {@link ByteBuf} </li>
 *     <li>{@link ByteBufHolder#copy()}：返回该 {@link ByteBufHolder} 的一个深拷贝，包括一个其所包含的 {@link ByteBuf} 的非共享副本</li>
 *     <li>{@link ByteBufHolder#duplicate()}：返回该 {@link ByteBufHolder} 的一个浅拷贝，包括一个其所包含的 {@link ByteBuf} 的共享副本</li>
 *     <li></li>
 * </ul>
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class ByteBufHolderDemo {

    public static void main(String[] args) {

        Charset utf8 = StandardCharsets.UTF_8;
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hi Netty", utf8);
        DefaultByteBufHolder bufHolder = new DefaultByteBufHolder(byteBuf);

        // 深拷贝
        ByteBufHolder copy = bufHolder.copy();
        System.out.println(copy.content().toString(utf8));

        // 浅拷贝
        ByteBufHolder duplicate = bufHolder.duplicate();
        ByteBuf buf = duplicate.content();
        buf.setByte(3, 'J');
        System.out.println(buf.toString(utf8));

        // 返回所持有的 ByteBuf
        ByteBuf content = bufHolder.content();
        System.out.println(content.toString(utf8));
    }
}
