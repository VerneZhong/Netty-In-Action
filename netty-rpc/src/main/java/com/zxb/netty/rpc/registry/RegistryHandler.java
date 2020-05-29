package com.zxb.netty.rpc.registry;

import com.zxb.netty.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.zxb
 * @date 2020-05-28 20:52:18
 */
public class RegistryHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 用于保存注册的服务
     */
    private static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();

    private List<String> classNames = new ArrayList<>();

    public RegistryHandler() {
        String packageName = "com.zxb.netty.rpc.provider";
        scannerClass(packageName);
        doRegister();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        InvokerProtocol request = (InvokerProtocol) msg;
        // 当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        if (registryMap.containsKey(request.getClassName())) {
            // 反射调用
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParams());
            Object result = method.invoke(clazz, request.getValues());
            ctx.writeAndFlush(result);
        }
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 递归扫描
     * @param packageName
     */
    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        if (url != null) {
            File dir = new File(url.getFile());
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                // 如果是一个文件夹，继续递归
                if (file.isDirectory()) {
                    scannerClass(packageName + "." + file.getName());
                } else {
                    classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
                }
            }
        }
    }

    /**
     * 注册服务
     */
    private void doRegister() {
        if (classNames.size() == 0) {
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> aClass = Class.forName(className);
                // 返回第一个接口名称
                Class<?> o = aClass.getInterfaces()[0];
                // 将服务实例信息添加到注册列表中
                registryMap.put(o.getName(), aClass.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
