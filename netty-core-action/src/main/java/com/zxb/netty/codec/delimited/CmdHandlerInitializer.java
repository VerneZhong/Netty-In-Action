package com.zxb.netty.codec.delimited;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * 使用 {@link io.netty.channel.ChannelInitializer} 安装解码器
 * @author Mr.zxb
 * @date 2020-05-14
 **/
public class CmdHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 空格分隔符
     */
    final byte SPACE = ' ';

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 添加 CmdDecoder 以提取 Cmd 对象，并将它转发给下一个 ChannelInboundHandler
                .addLast(new CmdDecoder(64 * 1024))
                // 添加 CmdHandler 以接收和处理 Cmd 对象
                .addLast(new CmdHandler());
    }

    /**
     * 重写了 {@link #decode(ChannelHandlerContext, ByteBuf)} 方法中获取一行字符串，并从它的内容构建一个 {@link Cmd} 的实例
     */
    private class CmdDecoder extends LineBasedFrameDecoder {
        public CmdDecoder(int maxLengthFrame) {
            super(maxLengthFrame);
        }

        @Override
        protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
            // 从 ByteBuf 中提取由行尾符序列分隔的帧
            ByteBuf decode = (ByteBuf) super.decode(ctx, buffer);

            if (decode == null) {
                return null;
            }

            // 查找第一个空格字符的索引。前面是命令名称，接着是参数
            int index = decode.indexOf(decode.readerIndex(), decode.writerIndex(), SPACE);
            // 使用包含有命令名称和参数的切片创建新的 Cmd 对象
            return new Cmd(decode.slice(decode.readerIndex(), index), decode.slice(index + 1, decode.writerIndex()));
        }
    }

    /**
     * 从 {@link CmdDecoder} 获取解码的 {@link Cmd} 对象，并对它进行一些处理
     */
    private class CmdHandler extends SimpleChannelInboundHandler<Cmd> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
            // 处理 Cmd 对象
            System.out.println(msg);
        }
    }

    /**
     * 将帧的内容存储在 {@link ByteBuf} 中，一个 {@link ByteBuf} 用于名称，一个用于参数
     */
    private class Cmd {
        /**
         * 名称
         */
        private final ByteBuf name;
        /**
         * 参数
         */
        private final ByteBuf args;


        public Cmd(ByteBuf name, ByteBuf args) {
            this.name = name;
            this.args = args;
        }

        public ByteBuf getName() {
            return name;
        }

        public ByteBuf getArgs() {
            return args;
        }

        @Override
        public String toString() {
            return "name: " + name.toString() + ", args: " + args.toString();
        }
    }
}
