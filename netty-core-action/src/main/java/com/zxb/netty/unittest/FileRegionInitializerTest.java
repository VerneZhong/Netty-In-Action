package com.zxb.netty.unittest;

import com.zxb.netty.codec.file.FileRegionInitializer;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.io.File;

/**
 * {@link FileRegionInitializer} 测试用例
 *
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class FileRegionInitializerTest {

    @Test
    public void testFileRegionInitializer() {

        File file = new File("/Users/zhongxuebin/Downloads/trojan-cli.zip");

        EmbeddedChannel channel = new EmbeddedChannel(new FileRegionInitializer());

        channel.writeInbound(file);

        channel.readInbound();
    }
}
