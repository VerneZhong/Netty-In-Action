package com.zxb.netty.channelhandler;

import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

/**
 * {@link ChannelPipeline} 示例
 * <p>
 *     <ul>
 *         <li>add...()：将一个 {@link ChannelHandler} 添加到 {@link ChannelPipeline} 中</li>
 *         <li>remove()：将一个 {@link ChannelHandler} 从 {@link ChannelPipeline} 中移除</li>
 *         <li>replace()：将 {@link ChannelPipeline} 中到一个 {@link ChannelHandler} 替换为另一个 {@link ChannelHandler}</li>
 *         <li>get()：通过类型或者名称返回 {@link ChannelHandler}</li>
 *         <li>context()：返回和 {@link ChannelHandler} 绑定的 {@link ChannelHandlerContext}</li>
 *         <li>names()：返回 {@link ChannelPipeline} 中所有 {@link ChannelHandler} 的名称</li>
 *     </ul>
 * </p>
 * @author Mr.zxb
 * @date 2020-04-28
 **/
public class ChannelPipelineDemo {

    public static void main(String[] args) {

        // 创建 ChannelPipeline
        Channel channel = new NioSocketChannel();
        ChannelPipeline channelPipeline = channel.pipeline();

        // 添加 ChannelHandler
        FirstHandler firstHandler = new FirstHandler();
        channelPipeline.addLast(firstHandler);
        channelPipeline.addFirst("handler2", new TwoHandler());
        channelPipeline.addLast("handler3", new ThirdHandler());

        // 移除 ChannelHandler
        channelPipeline.remove("handler3");
        channelPipeline.remove(firstHandler);

        // 替换 ChannelHandler
        channelPipeline.replace("handler2", "handler4", new FourHandler());

        // 获得指定 ChannelHandler
        ChannelHandler handler2 = channelPipeline.get("handler2");

        // 获取 ChannelHandler 绑定的 ChannelHandlerContext
        ChannelHandlerContext channelHandlerContext = channelPipeline.context(firstHandler);

        // ChannelPipeline 中的 ChannelHandler names
        List<String> channelHandlerNames = channelPipeline.names();
    }

    @Sharable
    static class FirstHandler extends ChannelInboundHandlerAdapter {

    }

    @Sharable
    static class TwoHandler extends ChannelInboundHandlerAdapter {

    }

    @Sharable
    static class ThirdHandler extends ChannelInboundHandlerAdapter {

    }

    @Sharable
    static class FourHandler extends ChannelInboundHandlerAdapter {

    }
}
