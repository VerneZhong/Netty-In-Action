package com.zxb.netty.unittest;

import com.zxb.netty.codec.CombinedByteCharCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link CombinedByteCharCodec} 测试用例
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class CombinedByteCharCodecTest {

    @Test
    public void testCodec() {
        EmbeddedChannel channel = new EmbeddedChannel(new CombinedByteCharCodec());

        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        ByteBuf outBound = Unpooled.copiedBuffer(chars, CharsetUtil.UTF_8);

        Assert.assertTrue(channel.writeOutbound(outBound));

        ByteBuf o = channel.readOutbound();

        while (o.readableBytes() >= 2) {
//            System.out.println(o.readChar());
            System.out.println(o.readByte());
        }
    }
}
