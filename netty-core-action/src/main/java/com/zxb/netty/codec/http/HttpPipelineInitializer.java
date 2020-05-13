package com.zxb.netty.codec.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.LastHttpContent;

/**
 * 构建基于 Netty 的Http应用。
 * <p>
 *     HTTP是基于请求/响应模式的，客户端向服务器发送一个 Http 请求，然后服务器将会返回一个 Http 响应，Netty提供了多种编码器和解码器
 *     以简化对 Http 协议对使用.
 * </p>
 * <p>
 *     一个 Http 请求/响应可能由多个数据部分组成，并且它总是以一个 {@link LastHttpContent} 部分作为结束.
 *     {@link FullHttpRequest} 和 {@link FullHttpResponse} 消息是特殊的子类型，分别代表了完整的请求和响应.
 *     所有类型的 Http 消息都实现了 {@link HttpObject} 接口.
 * </p>
 *  <ul>
 *      <li>{@link HttpRequestEncoder}：将 {@link HttpRequest}、{@link HttpContent}和 {@link LastHttpContent} 消息编码为字节</li>
 *      <li>{@link HttpResponseEncoder}：将 {@link HttpRequest}、{@link HttpContent}和 {@link LastHttpContent} 消息编码为字节</li>
 *      <li>{@link HttpRequestDecoder}：将字节解码为 {@link HttpRequest}、{@link HttpContent}和 {@link LastHttpContent} 消息</li>
 *      <li>{@link HttpResponseDecoder}：将字节解码为 {@link HttpRequest}、{@link HttpContent}和 {@link LastHttpContent} 消息</li>
 *  </ul>
 *
 * @author Mr.zxb
 * @date 2020-05-13
 **/
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {

    private final boolean isClient;

    public HttpPipelineInitializer(boolean isClient) {
        this.isClient = isClient;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if (isClient) {
            // 如果是客户端，则添加 HttpResponseDecoder 解码器，处理来自服务器的响应
            pipeline.addLast("decoder", new HttpResponseDecoder());
            // 客户端，添加 HttpRequestEncoder 编码器，处理客户端的请求，以向服务器发送请求
            pipeline.addLast("encoder", new HttpRequestEncoder());
        } else {
            // 如果是服务端，则添加 HttpRequestDecoder 解码器，以接收来自客户端的请求
            pipeline.addLast("decoder", new HttpRequestDecoder());
            // 服务端，则添加 HttpResponseEncoder 编码器，以向客户端发送响应
            pipeline.addLast("encoder", new HttpResponseEncoder());
        }
    }
}
