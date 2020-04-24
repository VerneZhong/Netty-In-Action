package com.zxb.java.nio.bytebuffer;

import java.nio.ByteBuffer;

/**
 * 基于Java NIO的 {@link ByteBuffer} 的复合缓冲区模式
 *
 * @author Mr.zxb
 * @date 2020-04-24
 **/
public class CompositeByteBuffer {

    public static void main(String[] args) {

        // Use an array to hold the message parts
        ByteBuffer header = null, body = null;
        ByteBuffer[] message = new ByteBuffer[]{header, body};

        // Create a new ByteBuffer and use copy to merge the header and body
        ByteBuffer message2 = ByteBuffer.allocate(header.remaining() + body.remaining());

        message2.put(header);
        message2.put(body);
        message2.flip();
    }
}
