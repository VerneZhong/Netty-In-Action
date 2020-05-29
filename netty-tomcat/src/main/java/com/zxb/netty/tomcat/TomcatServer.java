package com.zxb.netty.tomcat;

import com.zxb.netty.tomcat.handler.TomcatHandler;
import com.zxb.netty.tomcat.http.Servlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class
 *
 * @author Mr.zxb
 * @date 2020-05-29 14:07
 */
public class TomcatServer {

    private final int port = 8080;

    private final ConcurrentHashMap<String, Servlet> servletMapping = new ConcurrentHashMap<>();

    private final Properties webXml = new Properties();

    /**
     * 加载 web.xml，同时初始化 ServletMapping 对象
     * @throws Exception
     */
    private void init() throws Exception {
        String web_inf = this.getClass().getResource("/").getPath();
        FileInputStream inputStream = new FileInputStream(web_inf + "web.properties");
        webXml.load(inputStream);

        for (Object o : webXml.keySet()) {
            String key = o.toString();
            if (key.endsWith(".url")) {
                String servletName = key.replaceAll("\\.url$", "");
                String url = webXml.getProperty(key);
                String className = webXml.getProperty(servletName + ".className");
                Servlet servlet = (Servlet) Class.forName(className).newInstance();
                servletMapping.put(url, servlet);
            }
        }
    }

    public void start()  {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            init();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    // 绑定端口
                    .localAddress(port)
                    // 针对主线程的配置，分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对主线程的配置，保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline()
                                    // Http Response 编码器
                                    .addLast(new HttpResponseEncoder())
                                    // Http Request 解码器
                                    .addLast(new HttpRequestDecoder())
                                    // 业务逻辑处理
                                    .addLast(new TomcatHandler(servletMapping));
                        }
                    });

            // 启动服务
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("Tomcat 已启动，监听端口是：" + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TomcatServer().start();
    }
}
