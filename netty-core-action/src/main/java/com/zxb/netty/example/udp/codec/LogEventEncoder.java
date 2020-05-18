package com.zxb.netty.example.udp.codec;

import com.zxb.netty.example.udp.pojo.LogEvent;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 将 {@link LogEvent} 编码转换为 {@link DatagramPacket} 的编码器
 * @author Mr.zxb
 * @date 2020-05-18 21:57:51
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    /**
     * LogEventEncoder 创建了即将被发送到指定的 InetSocketAddress 的 DatagramPacket 消息
     */
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        Charset utf8 = CharsetUtil.UTF_8;

        byte[] file = logEvent.getLogFile().getBytes(utf8);
        byte[] msg = logEvent.getMsg().getBytes(utf8);

        ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);

        // 将文件名写入到 ByteBuf 中
        buf.writeBytes(file);
        // 添加一个分隔符
        buf.writeByte(LogEvent.SEPARATOR);
        // 将日志消息写入到 ByteBuf 中
        buf.writeBytes(msg);

        // 将一个拥有数据和目的地地址的新 DatagramPacket 添加到出站的消息列表中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
