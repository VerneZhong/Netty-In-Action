package com.zxb.netty.codec.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 自动聚合 HTTP 的消息片段，添加 {@link HttpObjectAggregator} 解码器
 * <p>
 *     由于 Http 的请求和响应可能由许多部分组成，因此需要聚合消息组成完整的消息。Netty提供了聚合器，可以将多个消息部分合并为
 *     {@link FullHttpRequest} 或者 {@link FullHttpResponse} 消息，通过这样的方式，总是可以看到完整的消息内容，
 *     由于消息分段需要被缓冲，直到可以转发一个完整的消息给下一个 {@link ChannelInboundHandler}，所以这个操作有轻微的开销，
 *     好处是不用关心消息碎片。
 *     引入这种自动聚合机制只不过是向 {@link ChannelPipeline} 添加另外一个 {@link ChannelHandler}，很简单便捷
 * </p>
 *
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class HttpAggregatorInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpAggregatorInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (isClient) {
            // 如果是客户端，则添加 HttpClientCodec
            pipeline.addLast("codec", new HttpClientCodec());
        } else {
            // 如果是服务端，则添加 HttpServerCodec
            pipeline.addLast("codec", new HttpServerCodec());
        }
        // 将最大消息大小设为 512KB 的 HttpObjectAggregator 添加到 ChannelPipeline 中
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
    }
}
