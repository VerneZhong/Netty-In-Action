package com.zxb.netty.unittest;

import com.zxb.netty.codec.delimited.LineBasedHandlerInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * {@link LineBasedHandlerInitializer} 分隔符的测试用例
 *
 * @author Mr.zxb
 * @date 2020-05-14
 **/
public class LineBasedHandlerInitializerTest {

    @Test
    public void testLineBasedHandler() {
        Charset utf8 = CharsetUtil.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("Hi\r\nzxb\r\nyou are have a lovely baby\r\n", utf8);

        EmbeddedChannel channel = new EmbeddedChannel(new LineBasedHandlerInitializer());

        channel.writeInbound(buf);
        channel.finish();

        channel.readInbound();
    }
}
