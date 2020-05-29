package com.zxb.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-05-29 14:09
 */
public class Request {

    private final ChannelHandlerContext ctx;
    private final HttpRequest request;

    public Request(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getUrl() {
        return request.uri();
    }

    public String getMethod() {
        return request.method().name();
    }

    public Map<String, List<String>> getParameters() {
        return new QueryStringDecoder(request.uri()).parameters();
    }

    public String getParameter(String name) {
        Map<String, List<String>> parameters = getParameters();
        List<String> param = parameters.get(name);
        if (param != null) {
            return param.get(0);
        }
        return null;
    }
}
