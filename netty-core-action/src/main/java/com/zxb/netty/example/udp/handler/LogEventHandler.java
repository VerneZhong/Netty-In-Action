package com.zxb.netty.example.udp.handler;

import com.zxb.netty.example.udp.pojo.LogEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 扩展 {@link SimpleChannelInboundHandler} 以处理 {@link LogEvent} 消息
 * @author Mr.zxb
 * @date 2020-05-19
 **/
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogEvent msg) throws Exception {
        // 输出 LogEvent 信息
        StringBuilder builder = new StringBuilder();
        builder.append(msg.getReceivedTimestamp())
                .append(" [")
                .append(msg.getSource().toString())
                .append("] [")
                .append(msg.getLogFile())
                .append("] : ")
                .append(msg.getMsg());
        System.out.println(builder.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
