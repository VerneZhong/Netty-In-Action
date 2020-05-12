package com.zxb.netty.unittest;

import com.zxb.netty.codec.IntegerToStringDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * {@link IntegerToStringDecoder} 测试用例
 * @author Mr.zxb
 * @date 2020-05-11
 **/
public class IntegerToStringDecoderTest {

    @Test
    public void testIntegerToStringDecoder() throws UnsupportedEncodingException {
        ByteBuf buffer = Unpooled.buffer();

        for (int i = 1; i < 5; i++) {
            buffer.writeInt(i);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringDecoder());

        Assert.assertTrue(channel.writeInbound(buffer));
        Assert.assertTrue(channel.finish());

        ByteBuf readInbound = channel.readInbound();

        byte[] bytes = new byte[buffer.readableBytes()];
        int index = 0;
        while (readInbound.readableBytes() > 0) {
            bytes[index] = readInbound.readByte();
            index++;
        }

        System.out.println(Arrays.toString(bytes));
    }
}
