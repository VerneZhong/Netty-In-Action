package com.zxb.netty.tomcat.http;

import io.netty.handler.codec.http.HttpMethod;

/**
 * Servlet 请求处理
 *
 * @author Mr.zxb
 * @date 2020-05-29 14:08
 */
public abstract class Servlet {

    public void service(Request request, Response response) throws Exception {
        if (HttpMethod.GET.name().equals(request.getMethod())) {
            doGet(request, response);
        } else if (HttpMethod.POST.name().equals(request.getMethod())) {
            doPost(request, response);
        }
    }

    /**
     * Get 方法处理
     * @param request
     * @param response
     * @throws Exception
     */
    public abstract void doGet(Request request, Response response) throws Exception;

    /**
     * Post 方法处理
     * @param request
     * @param response
     * @throws Exception
     */
    public abstract void doPost(Request request, Response response) throws Exception;
}
