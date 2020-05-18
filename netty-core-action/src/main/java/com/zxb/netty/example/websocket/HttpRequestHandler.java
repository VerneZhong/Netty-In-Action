package com.zxb.netty.example.websocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 扩展 {@link SimpleChannelInboundHandler} 处理 Http 请求消息
 *
 * @author Mr.zxb
 * @date 2020-05-18
 **/
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    private static final File INDEX;

    static {
        try {
            URL location = HttpRequestHandler.class.getProtectionDomain()
                    .getCodeSource().getLocation();
            String path = location.toURI() + "html/index.html";
//            String path = "/Users/zhongxuebin/IdeaProjects/Netty-In-Action/netty-core-action/src/main/java/com/zxb/netty/example/html/index.html";
            // 去掉 file: 前缀
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    public HttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        // 如果请求的是 WebSocket协议升级，则增加引用计数，并将它传递给下一个 ChannelInboundHandler
        // 因为是异步操作，防止 channelRead0 执行完释放掉了 request，所以需要将引用计数增加1，防止被释放掉引用
        if (request.uri().equalsIgnoreCase(wsUri)) {
            ctx.fireChannelRead(request.retain());
        } else {
            // 处理 100 Continue 请求以符合 HTTP 1.1 规范
            if (HttpUtil.is100ContinueExpected(request)) {
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                ctx.writeAndFlush(response);
            }

            // 读取 index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");

            HttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

            // 如果请求了 keep-alive，则添加所需要的 HTTP 头信息
            boolean keepAlive = HttpUtil.isKeepAlive(request);
            if (keepAlive) {
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }

            // 将 HttpResponse 写到客户端
            ctx.write(response);

            // 将 index.html 写到客户端
            if (ctx.pipeline().get(SslHandler.class) == null) {
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            } else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }

            // 将 LastHttpContent 写到客户端并刷新之前所有写入的消息
            ChannelFuture channelFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

            // 如果没有请求 keep-alive，则在写操作完成后关闭 Channel
            if (!keepAlive) {
                channelFuture.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
