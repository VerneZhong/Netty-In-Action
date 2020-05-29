package com.zxb.netty.tomcat.handler;

import com.zxb.netty.tomcat.http.Request;
import com.zxb.netty.tomcat.http.Response;
import com.zxb.netty.tomcat.http.Servlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Tomcat 处理 Http 逻辑
 *
 * @author Mr.zxb
 * @date 2020-05-29 14:24
 */
public class TomcatHandler extends SimpleChannelInboundHandler<HttpRequest> {

    private final ConcurrentHashMap<String, Servlet> servletMapping;

    public TomcatHandler(ConcurrentHashMap<String, Servlet> servletMapping) {
        this.servletMapping = servletMapping;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
        // 交给自己实现的 Request
        Request request = new Request(ctx, msg);
        // 交给自己的 Response 实现
        Response response = new Response(ctx, msg);

        String url = request.getUrl();

        if (servletMapping.containsKey(url)) {
            servletMapping.get(url).service(request, response);
        } else {
            response.write("404 - Not Found");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
