package com.zxb.netty.codec;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 自定义 {@link CombinedChannelDuplexHandler}，通过该解码器和编码器实现参数化 {@link CombinedByteCharCodec}
 *
 * @author Mr.zxb
 * @date 2020-05-12
 **/
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharacterDecoder, CharacterToByteEncoder> {
    public CombinedByteCharCodec() {
        // 将委托实例传递给父类
        super(new ByteToCharacterDecoder(), new CharacterToByteEncoder());
    }
}
