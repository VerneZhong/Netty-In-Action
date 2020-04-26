package com.zxb.java.nio.bytebuffer;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Java char byte 操作 <p/>
 * char 是字符，byte 是字节，都可以强制转换为一个整数。(char 强制转换为整数表示 这个字符对应的  Unicode 码的位置) <p/>
 * char 是无符号型的，大小范围为 0 -66535 （对应的 Unicode 码位置） <p/>
 * byte 是字节 ，有符号型的，大小范围是  -128-127 <p/>
 * char 可以表示中文，因为Unicode编码中包含了中文  <p/>
 * byte 不可以表示中文  <p/>
 * char 转换为 byte    <p/>
 *
 * @author Mr.zxb
 * @date 2020-04-26
 **/
public class CharAndByteOperation {

    public static void main(String[] args) {

        char c = 'z';

        CharBuffer charBuffer = CharBuffer.allocate(1);

        charBuffer.put(c);

        charBuffer.flip();

        ByteBuffer buffer = StandardCharsets.UTF_8.encode(charBuffer);

        byte[] bytes = buffer.array();

        for (byte aByte : bytes) {
            System.out.print((char) aByte);
        }
    }
}
