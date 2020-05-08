package com.zxb.netty.unittest;

import com.zxb.netty.codec.AbsIntegerEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 测试 {@link AbsIntegerEncoder} 编码器，测试出站消息。
 * <p>
 *     <ol>
 *     步骤：
 *         <li>将4字节的负整数写到一个新的 {@link ByteBuf} 中</li>
 *         <li>创建一个 {@link EmbeddedChannel}, 并为它分配一个 {@link AbsIntegerEncoder}</li>
 *         <li>调用 {@link EmbeddedChannel} 上的 {@link EmbeddedChannel#writeOutbound(Object...)} 方法来写入该 {@link ByteBuf}</li>
 *         <li>标记该 {@link Channel} 为已完成状态</li>
 *         <li>从 {@link EmbeddedChannel} 的出站端读取所有的整数，并验证是否已产生了绝对值</li>
 *     </ol>
 * </p>
 * @author Mr.zxb
 * @date 2020-05-08
 **/
public class AbsIntegerEncoderTest {

    @Test
    public void testEncoded() {
        // 创建一个 ByteBuf，并且写入 9 个负整数
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 1; i < 10; i++) {
            buffer.writeInt(i * -1);
        }

        // 创建一个 EmbeddedChannel，并安装一个要测试的 AbsIntegerEncoder
        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());

        // 写入 ByteBuf，并断言调用 readOutbound()方法将会产生数据
        assertTrue(channel.writeOutbound(buffer));
        // 将该 Channel 标记为已完成状态
        assertTrue(channel.finish());

        // 读取所产生的消息，并断言它们包含了对应的绝对值
        for (int i = 1; i < 10; i++) {
            assertEquals(i, (int) channel.readOutbound());
        }
        assertNull(channel.readOutbound());
    }
}
