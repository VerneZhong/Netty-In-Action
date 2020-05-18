package com.zxb.netty.example.websocket.secure;

import com.zxb.netty.example.websocket.ChatServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 扩展 {@link ChatServer} 以支持安全加密
 * @author Mr.zxb
 * @date 2020-05-18 21:27:49
 */
public class SecureChatServer extends ChatServer {

    private final SslContext sslContext;

    public SecureChatServer(SslContext sslContext) {
        this.sslContext = sslContext;
    }

    @Override
    protected ChannelInitializer<Channel> createChannelInitializer(ChannelGroup channelGroup) {
        return new SecureChatServerInitializer(channelGroup, sslContext);
    }

    public static void main(String[] args) throws Exception {
        SelfSignedCertificate certificate = new SelfSignedCertificate();

        SecureChatServer chatServer = new SecureChatServer(SslContextBuilder
                .forServer(certificate.certificate(), certificate.privateKey())
                .build());
        ChannelFuture channelFuture = chatServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(chatServer::destroy));

        channelFuture.channel().closeFuture().syncUninterruptibly();
    }
}
