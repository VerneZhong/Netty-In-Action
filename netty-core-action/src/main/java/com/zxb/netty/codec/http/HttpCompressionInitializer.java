package com.zxb.netty.codec.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * HTTP 压缩：当 Http 使用时，建议开启压缩功能能尽可能多地减小传输数据的大小，Netty 提供了压缩 {@link HttpContentCompressor} 和
 * 解压缩 {@link HttpContentDecompressor} 的 {@link ChannelHandler} 实现，同时支持 gzip 和 deflate 编码.
 *
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpCompressionInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (isClient) {
            // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
            // 如果是客户端，则添加 HttpContentDecompressor 以处理来自服务器的压缩内容
            pipeline.addLast("decompressor", new HttpContentDecompressor());
        } else {
            // 如果是服务端，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
            // 如果是服务端，则添加 HttpContentCompressor 来压缩数据
            pipeline.addLast("compressor", new HttpContentCompressor());
        }
    }
}
