package com.zxb.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-05-29 14:09
 */
public class Response {

    private final ChannelHandlerContext ctx;
    private final HttpRequest request;

    public Response(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public void write(String s) {
        if (s == null || s.length() == 0) {
            return;
        }
        FullHttpResponse response = new DefaultFullHttpResponse(
                request.protocolVersion(),
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(s.getBytes(CharsetUtil.UTF_8)));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;");
        ctx.writeAndFlush(response);
        ctx.close();
    }
}
