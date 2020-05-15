package com.zxb.netty.codec.serialization.jboss;

import io.netty.channel.*;
import io.netty.handler.codec.marshalling.*;

import java.io.Serializable;

/**
 * 使用 JBoss Marshalling 序列化
 * <ul>
 *     <li>{@link CompatibleMarshallingDecoder}：与只使用 JDK 序列化的远程节点兼容</li>
 *     <li>{@link CompatibleMarshallingEncoder}：与只使用 JDK 序列化的远程节点兼容</li>
 *     <li>{@link MarshallingDecoder}：适用于使用 JBoss Marshalling 的节点，这些类必须一起使用</li>
 *     <li>{@link MarshallingEncoder}：适用于使用 JBoss Marshalling 的节点，这些类必须一起使用</li>
 * </ul>
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class JBossMarshallingInitializer extends ChannelInitializer<Channel> {

    private final MarshallerProvider marshallerProvider;
    private final UnmarshallerProvider unmarshallerProvider;

    public JBossMarshallingInitializer(MarshallerProvider marshallerProvider, UnmarshallerProvider unmarshallerProvider) {
        this.marshallerProvider = marshallerProvider;
        this.unmarshallerProvider = unmarshallerProvider;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 添加 MarshallingDecoder 以将 ByteBuf 转换为 POJO
        pipeline.addLast(new MarshallingDecoder(unmarshallerProvider));
        // 添加 MarshallingEncoder 以将 POJO 转换为 ByteBuf
        pipeline.addLast(new MarshallingEncoder(marshallerProvider));
        // 添加 ObjectHandler，以处理普通的实现了 Serializable 接口的 POJO
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Serializable> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Serializable msg) throws Exception {
            // Do something
            System.out.println(msg.toString());
        }
    }
}
