package com.zxb.java.executor;

import java.util.concurrent.*;

/**
 * JDK {@link ScheduledExecutorService} 示例
 * @author Mr.zxb
 * @date 2020-04-30
 **/
public class ScheduleExecutorServiceDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        // 创建 ScheduledExecutorService 线程池
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(4);

        // 执行调度任务
        ScheduledFuture<?> future = executorService.schedule(() -> System.out.println("60 seconds later"), 60, TimeUnit.SECONDS);

        // 等待任务执行结束
        future.get();

        // 任务执行完毕后关闭线程池
        executorService.shutdown();
    }
}
