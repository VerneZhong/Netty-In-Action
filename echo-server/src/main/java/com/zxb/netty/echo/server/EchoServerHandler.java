package com.zxb.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;

import java.nio.charset.StandardCharsets;

/**
 * 定义响应接入事件处理器，继承自 {@link ChannelHandler} 接口的 {@link ChannelInboundHandlerAdapter} 的实现类
 *
 * @author Mr.zxb
 * @date 2020-03-22 19:34
 */
@Sharable // 表示一个 ChannelHandler 可以被多个 Channel 安全地共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当有消息传入会调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Server received: " + byteBuf.toString(StandardCharsets.UTF_8));
        // 向客户端发送消息，而不冲刷出站消息
        ctx.write(byteBuf);
    }

    /**
     * 通知 {@link ChannelInboundHandler} 最后一次对 {@link ChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}
     * 的调用是当前批量读取中的最后一条消息 <p/>
     * 也就是说最后一次读取消息时会调用该方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未处理的消息刷新到远程节点，并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 读取操作期间，有异常抛出时会调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 输出异常栈跟踪
        cause.printStackTrace();
        // 关闭该 Channel
        ctx.close();
    }
}
