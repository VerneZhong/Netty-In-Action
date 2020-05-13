package com.zxb.netty.codec.ssl;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 添加 SSL/TLS 支持
 *
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class SslChannelInitializer extends ChannelInitializer<Channel> {

    /**
     * 要使用的 SslContext
     */
    private final SslContext sslContext;

    /**
     * 如果设置为true，第一个写入的消息将不会被加密（客户度应该设置为true）
     */
    private final boolean startTls;

    public SslChannelInitializer(SslContext sslContext, boolean startTls) {
        this.sslContext = sslContext;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 对于每个 SslHandler 实例，都使用 Channel 的 ByteBufAllocator 从 SslContext 获取一个新的 SslEngine
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        // 将 SslHandler 作为第一个 ChannelHandler 添加到 ChannelPipeline 中
        ch.pipeline().addFirst("ssl", new SslHandler(sslEngine, startTls));
    }
}
