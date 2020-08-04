package com.zxb.java.executor;

import java.util.concurrent.*;

/**
 * {@link FutureTask} {@link Callable} 示例
 * @author Mr.zxb
 * @date 2020-08-04 21:17:35
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        print("start...");

        // 方式一：通过 executorService 提交一个异步线程
//        Future<String> future = executorService.submit(() -> {
//            sleep(2);
//            return "hello callable 1";
//        });

        // 方式二：通过 FutureTask 包装异步线程的返回，返回结果在 FutureTask 中获取而不是 在提交线程中
        FutureTask<String> future = new FutureTask<>(() -> {
            sleep(3);
            return "hello callable 2";
        });

        executorService.submit(future);

        System.out.println(future.get());

        print("end...");

//        executorService.shutdown();
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
