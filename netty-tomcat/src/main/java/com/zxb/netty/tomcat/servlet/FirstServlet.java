package com.zxb.netty.tomcat.servlet;

import com.zxb.netty.tomcat.http.Request;
import com.zxb.netty.tomcat.http.Response;
import com.zxb.netty.tomcat.http.Servlet;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-05-29 15:30
 */
public class FirstServlet extends Servlet {
    @Override
    public void doGet(Request request, Response response) throws Exception {
        this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) throws Exception {
        response.write("This is First Servlet");
    }
}
