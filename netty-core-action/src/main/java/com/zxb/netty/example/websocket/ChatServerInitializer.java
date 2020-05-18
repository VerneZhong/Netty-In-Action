package com.zxb.netty.example.websocket;

import com.zxb.netty.codec.websocket.server.WebSocketIndexPageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * 初始化 {@link ChannelInitializer}
 * @author Mr.zxb
 * @date 2020-05-18
 **/
public class ChatServerInitializer extends ChannelInitializer<Channel> {

    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 将所有需要的 ChannelHandler 添加到 ChannelPipeline 中
        ch.pipeline()
                // 添加 HttpSever 编解码，编解码 HttpRequest、HttpContent和LastHttpContent
                .addLast(new HttpServerCodec())
                // 聚合 Http 请求和响应
                .addLast(new HttpObjectAggregator(64 * 1024))
                // 处理 WebSocket 握手、Ping、Pong和Close等任务
                .addLast(new WebSocketServerProtocolHandler("/ws"))
                // 处理 FullHttpRequest
                .addLast(new WebSocketIndexPageHandler("/ws"))
                // 处理 WebSocket 文本帧和握手完成事件
                .addLast(new TextWebSocketFrameHandler(group));
    }
}
