package com.zxb.netty.tomcat.servlet;

import com.zxb.netty.tomcat.http.Request;
import com.zxb.netty.tomcat.http.Response;
import com.zxb.netty.tomcat.http.Servlet;

/**
 * Servlet 实现
 *
 * @author Mr.zxb
 * @date 2020-05-29 15:31
 */
public class SecondServlet extends Servlet {
    @Override
    public void doGet(Request request, Response response) throws Exception {
        this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
        response.write("This is Second Servlet");
    }
}
