package com.zxb.netty.unittest;

import com.zxb.netty.codec.ToIntegerDecoder;
import com.zxb.netty.codec.ToIntegerReplayingDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

/**
 * {@link ToIntegerDecoder} 解码器测试
 * @author Mr.zxb
 * @date 2020-05-09
 **/
public class ToIntegerDecoderTest {

    @Test
    public void testToIntegerDecoder() {
        ByteBuf buffer = Unpooled.buffer();

        for (int i = 0; i < 10; i++) {
            buffer.writeInt(i);
        }

        ByteBuf duplicate = buffer.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel(new ToIntegerReplayingDecoder());

        Assert.assertTrue(channel.writeInbound(duplicate));
        Assert.assertTrue(channel.finish());

        // int 4 个字节长度
        int i = buffer.readableBytes() / 4;
        for (int j = 0; j < i; j++) {
            Object read = channel.readInbound();
            System.out.print(read);
        }

    }
}
