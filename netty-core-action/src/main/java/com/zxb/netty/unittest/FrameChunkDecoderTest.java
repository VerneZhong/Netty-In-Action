package com.zxb.netty.unittest;

import com.zxb.netty.codec.FrameChunkDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.TooLongFrameException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试 {@link FrameChunkDecoder} 解码器异常处理
 *
 * @author Mr.zxb
 * @date 2020-05-08
 **/
public class FrameChunkDecoderTest {

    @Test
    public void     testFrameDecoded() {
        // 创建 ByteBuf，写入9个字节的数据
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        // 创建 ByteBuf 缓冲区副本
        ByteBuf input = buffer.duplicate();

        // 创建一个 EmbeddedChannel，并安装一个帧大小为3字节的 FrameChunkDecoder
        EmbeddedChannel channel = new EmbeddedChannel(new FrameChunkDecoder(3));

        // channel 写入 ByteBuf 的2个字节
        assertTrue(channel.writeInbound(input.readBytes(2)));

        try {
            // Channel 写入 4帧的字节数据，将会抛出超长的运行时异常
            channel.writeInbound(input.readBytes(4));
            Assert.fail();
        } catch (TooLongFrameException e) {
            e.printStackTrace();
        }

        // 写入剩余的3字节，并断言产生一个新的有效帧
        assertTrue(channel.writeInbound(input.readBytes(3)));
        // 将 Channel 标记已完成状态
        assertTrue(channel.finish());

        // 读取 Channel的消息，并验证值
        ByteBuf read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.skipBytes(4).readBytes(2), read);
        read.release();
        buffer.release();
    }
}
