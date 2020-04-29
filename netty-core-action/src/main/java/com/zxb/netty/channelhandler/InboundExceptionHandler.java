package com.zxb.netty.channelhandler;

import io.netty.channel.*;

/**
 * {@link ChannelInboundHandlerAdapter} 基本的入站异常处理
 * <p>
 *     <ul>
 *         <li> {@link ChannelInboundHandlerAdapter#exceptionCaught(ChannelHandlerContext, Throwable)} </li>
 *         <li>默认实现是简单的将当前异常转发给 {@link ChannelPipeline} 中的下一个 {@link ChannelHandler}</li>
 *         <li>如果异常到达了 {@link ChannelPipeline}末端，它将会被记录为未被处理</li>
 *         <li>要定义自定义的处理逻辑，重写exceptionCaught() 方法，然后决定是否将该异常传播出去</li>
 *     </ul>
 * </p>
 * @author Mr.zxb
 * @date 2020-04-29
 **/
public class InboundExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 打印异常堆栈
        cause.printStackTrace();
        // 关闭资源
        ctx.close();
    }
}
