package com.zxb.netty.example.udp.codec;

import com.zxb.netty.example.udp.pojo.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.List;

/**
 * 将 {@link DatagramPacket} 编码转换为 {@link LogEvent} 的解码器
 *
 * @author Mr.zxb
 * @date 2020-05-19
 **/
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket msg, List<Object> out) throws Exception {
        // 获取对 DatagramPacket 中的数据（ByteBuf）的引用
        ByteBuf data = msg.content();
        // 获取分隔符的索引
        int idx = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR);
        Charset utf8 = CharsetUtil.UTF_8;

        // 提取文件名
        String filename = data.slice(0, idx).toString(utf8);

        // 提取日志信息
        String logMsg = data.slice(idx + 1, data.readableBytes()).toString(utf8);

        // 构建一个新的 LogEvent 对象，并且将它添加到列表中
        LogEvent event = new LogEvent(msg.sender(), System.currentTimeMillis(), filename, logMsg);
        out.add(event);
    }
}
