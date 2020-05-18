package com.zxb.netty.example.websocket.secure;

import com.zxb.netty.example.websocket.ChatServerInitializer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * 扩展 {@link ChatServerInitializer} 以添加加密
 * @author Mr.zxb
 * @date 2020-05-18 21:24:15
 */
public class SecureChatServerInitializer extends ChatServerInitializer {

    private final SslContext sslContext;

    public SecureChatServerInitializer(ChannelGroup group, SslContext sslContext) {
        super(group);
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        super.initChannel(ch);
        SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
        sslEngine.setUseClientMode(false);
        // 将 SSLHandler 添加到 ChannelPipeline 中
        ch.pipeline().addFirst(new SslHandler(sslEngine));
    }
}
