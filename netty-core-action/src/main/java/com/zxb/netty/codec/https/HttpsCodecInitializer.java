package com.zxb.netty.codec.https;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import java.io.File;

/**
 * 基于 Netty 构建 Https 应用
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class HttpsCodecInitializer extends ChannelInitializer<Channel> {

    private final SslContext sslContext;
    private final boolean isClient;

    public HttpsCodecInitializer(boolean isClient) throws SSLException {
        if (isClient) {
            this.sslContext = SslContextBuilder.forClient().build();
        } else {
            File keyCertChainFile = new File("");
            File keyFile = new File("");
            this.sslContext = SslContextBuilder.forServer(keyCertChainFile, keyFile).build();
        }
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());

        // 将 SslHandler 添加到 ChannelPipeline 中使用 Https
        pipeline.addFirst("ssl", new SslHandler(sslEngine));

        if (isClient) {
            // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 如果是服务端，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
    }
}
