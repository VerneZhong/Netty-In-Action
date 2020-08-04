package com.zxb.netty.promise;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.TimeUnit;

/**
 * {@link io.netty.util.concurrent.Promise} 示例
 * @author Mr.zxb
 * @date 2020-08-04 21:17:35
 */
public class PromiseDemo {

    public static void main(String[] args) {
        PromiseDemo promiseDemo = new PromiseDemo();
        Promise<String> promise = promiseDemo.doPromise("hello Promise");
        promise.addListener(future -> {
            System.out.println(promise.get());
        });
    }

    private Promise<String> doPromise(String value) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Promise<String> promise = new DefaultPromise<>(eventLoopGroup.next());
        eventLoopGroup.schedule(() -> {
            sleep(1);
//            promise.setSuccess("执行成功：" + value);
            promise.trySuccess("执行成功：" + value);
//            return promise;
        }, 0, TimeUnit.SECONDS);
        return promise;
    }

    private static <T> void print(T t) {
        System.out.println(t);
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
