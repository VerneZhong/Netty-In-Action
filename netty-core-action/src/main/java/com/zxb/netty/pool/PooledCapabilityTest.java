package com.zxb.netty.pool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * 内存池的性能优势，经过对比发现，采用内存池模式创建的 {@link ByteBuf} 性能比传统非池化方式高好几倍。
 * 在 {@code API Gateway}、RPC和流石处理框架中，请求和响应消息对象往往是"朝生夕灭"，特别是频繁地申请和释放大块 {@link Byte} 数组，
 * 会加重GC的负担及加大CPU资源占用率，通过内存池技术重用这些临时对象，可以降低GC次数和减少耗时，同时提升系统吞吐量。
 *
 * @author Mr.zxb
 * @date 2020-06-03 10:34
 */
public class PooledCapabilityTest {

    /**
     * 通过非内存池模式创建的 {@link ByteBuf} 性能测试
     */
    static void unPooledTest() {
        // 非内存池模式
        long beginTime = System.currentTimeMillis();
        ByteBuf buf;
        // 执行1亿次
        int maxTimes = 100_000_000;
        for (int i = 0; i < maxTimes; i++) {
            buf = Unpooled.buffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time: " + (System.currentTimeMillis() - beginTime) / 1000 + "s");
    }

    static void pooledTest() {
        // 内存池模式，非直接内存模式
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        long beginTime = System.currentTimeMillis();
        ByteBuf buf;
        // 执行1亿次
        int maxTimes = 100_000_000;
        for (int i = 0; i < maxTimes; i++) {
            buf = allocator.heapBuffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time: " + (System.currentTimeMillis() - beginTime) / 1000 + "s");

    }

    public static void main(String[] args) {
        // 非内存池性能测试
//        unPooledTest();

        // 内存池性能测试
        pooledTest();
    }
}
