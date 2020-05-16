package com.zxb.netty.codec.serialization.protobuf;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 基于 Google ProtoBuf 序列化
 * <ul>
 *     <li>{@link ProtobufDecoder}：使用 protobuf 对消息进行解码</li>
 *     <li>{@link ProtobufEncoder}：使用 protobuf 对消息进行编码</li>
 *     <li>{@link ProtobufVarint32FrameDecoder}：根据消息中的 Google Protocol Buffers 的" Base 128 Varints" 整型长度字段值动态地分割所接收到的 {@link ByteBuf}</li>
 *     <li>{@link ProtobufVarint32LengthFieldPrepender}：向 {@link ByteBuf} 前追加一个 Google Protocol Buffers 的" Base 128 Varints" 整型的长度字段值</li>
 * </ul>
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class ProtoBufInitializer extends ChannelInitializer<Channel> {

    private final MessageLite lite;

    public ProtoBufInitializer(MessageLite lite) {
        this.lite = lite;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 添加 ProtobufVarint32FrameDecoder 以分隔帧
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufEncoder());
        pipeline.addLast(new ProtobufDecoder(lite));
        pipeline.addLast(new ObjectHandler());
    }

    public static final class ObjectHandler extends SimpleChannelInboundHandler<Object> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // Do something with the object
            System.out.println(msg);
        }
    }
}
