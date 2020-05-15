package com.zxb.netty.codec.file;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.handler.stream.ChunkedNioStream;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * 使用 {@link ChunkedStream} 传输文件内容  <br/>
 *
 * <ul>
 *     <li>{@link ChunkedFile}：从文件中逐块读取数据，当平台不支持零拷贝或转换数据时使用</li>
 *     <li>{@link ChunkedNioFile}：和 {@link ChunkedFile} 类似，只是使用了 {@link FileChannel}</li>
 *     <li>{@link ChunkedStream}：从 {@link InputStream} 中逐块传输内容</li>
 *     <li>{@link ChunkedNioStream}：从 {@link ReadableByteChannel} 中逐块传输内容</li>
 * </ul>
 * @author Mr.zxb
 * @date 2020-05-15
 **/
public class ChunkedWriteHandlerInitializer extends ChannelInitializer<Channel> {

    private final File file;

    private final SslContext sslContext;

    public ChunkedWriteHandlerInitializer(File file, SslContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                // 将 SslHandler 添加到 ChannelPipeline 中
                .addLast(new SslHandler(sslContext.newEngine(ch.alloc())))
                // 添加 ChunkedWriteHandler 以处理作为 ChunkedInput 传入的数据
                .addLast(new ChunkedWriteHandler())
                // 一旦连接建立，WriteStreamHandler 就开始写文件数据
                .addLast(new WriteStreamHandler(new FileInputStream(file)));
    }

    public static final class WriteStreamHandler extends ChannelInboundHandlerAdapter {

        private final InputStream in;

        public WriteStreamHandler(InputStream in) {
            this.in = in;
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            // 当连接建立时，将使用 ChunkedInput 写文件数据
            ctx.writeAndFlush(new ChunkedStream(in));
        }
    }
}
