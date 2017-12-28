package com.javahe.jandroidglide.load.engine.executor;

import android.os.Process;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zfc on 2017/12/28.
 */

public class FifoPriorityThreadPoolExecutor extends ThreadPoolExecutor {
    private static final String TAG = "PriorityExecutor";

    private UncaughtThrowableStrategy uncaughtThrowableStrategy;

    public FifoPriorityThreadPoolExecutor(int poolSize){
        this(poolSize, UncaughtThrowableStrategy.LOG);
    }

    public FifoPriorityThreadPoolExecutor(int poolSize, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        this(poolSize, poolSize, 0, TimeUnit.MILLISECONDS, new DefaultThreadFactory(), uncaughtThrowableStrategy);
    }

    public FifoPriorityThreadPoolExecutor(int poolSize, int maxnumPoolSize, int keepAlive, TimeUnit timeUnit,
                                          ThreadFactory threadFactory, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        super(poolSize, maxnumPoolSize, keepAlive, timeUnit, new PriorityBlockingQueue<Runnable>(), threadFactory);
        this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
    }


    public enum UncaughtThrowableStrategy{
        INGORE,
        LOG{
            @Override
            protected void handle(Throwable throwable) {
                if (Log.isLoggable(TAG, Log.ERROR)) {
                    Log.e(TAG, "Request threw uncaught throwables , " + throwable);
                }
            }
        },
        THROW{
            @Override
            protected void handle(Throwable throwable) {
                super.handle(throwable);
                throw  new RuntimeException(throwable);
            }
        };


        protected void handle(Throwable throwable) {

        }
    }

    /**
     * 创建默认级别的线程
     */
    public static class DefaultThreadFactory implements ThreadFactory {
        int threadNum = 0;
        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            final Thread result = new Thread(runnable, "fifo-pool-thread" + threadNum) {
                @Override
                public void run() {
                    //设置线程优先级
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    super.run();
                }
            };
            threadNum++;
            return result;
        }
    }

}
