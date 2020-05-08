package com.zxb.netty.unittest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试 {@link CustomFixedLengthFrameDecoder}
 * @author Mr.zxb
 * @date 2020-05-07 22:06:30
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void testFrameDecoder() {
        // 创建一个 ByteBuf 并存储9字节
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();

        // 创建一个 EmbeddedChannel，并添加一个 CustomFixedLengthFrameDecoder，其将以3个字节的帧长度被测试
        EmbeddedChannel channel = new EmbeddedChannel(new CustomFixedLengthFrameDecoder(3));

        // 将数据写入 EmbeddedChannel
        assertTrue(channel.writeInbound(input.retain()));
        // 标记 Channel 为已完成状态
        assertTrue(channel.finish());

        // 读取所生成的消息，并且验证是否有3帧（切片），其中每帧（切片）都为3个字节
        ByteBuf read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buffer.release();
    }

    @Test
    public void testFramesDecoder2() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf duplicate = buffer.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new CustomFixedLengthFrameDecoder(3));

        // 返回false，因为没有一个完整的可供读取的帧
        assertFalse(channel.writeInbound(duplicate.readBytes(2)));
        assertTrue(channel.writeInbound(duplicate.readBytes(7)));

        assertTrue(channel.finish());

        ByteBuf read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());
        buffer.release();
    }

}
