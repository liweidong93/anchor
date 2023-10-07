package com.lwd.anchor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: liweidong
 * @data: 2023/9/26
 */
public class TaskExecutorManager {

    //获得cpu密集型线程池，因为占据CPU的时间片过多的话影响性能，所以这里控制了最大并发，防止主线程时间片减少
    //cpu密集型线程池
    public ThreadPoolExecutor cpuThreadPoolExecutor;
    //获取ios密集型线程池，有好多任务其实占用的cpu time非常少，所以使用缓存线程池，基本上来者不拒
    //io密集型线程池
    public ExecutorService ioThreadPoolExecutor;
    //线程池队列
    private BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue<>();
    //这个是为了保障任务超出BlockingQueue的最大值，且线程池中的线程数已经达到MAXIMUM_POOL_SIZE时候，还有任务到来会采用任务拒绝策略，这里定义的策略就是再开一个缓存线程池来执行。当然BlockingQueue默认的最大值为int_max，所以理论是用不到
    private RejectedExecutionHandler mRejectHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Executors.newCachedThreadPool().execute(r);
        }
    };
    //CPU核数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 5));
    //线程池最大线程数
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    //线程空置回收时间
    private static final int KEEP_ALIVE_SECONDS = 5;
    private static TaskExecutorManager INSTANCE = new TaskExecutorManager();

    private TaskExecutorManager(){
        cpuThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, mPoolWorkQueue, Executors.defaultThreadFactory(), mRejectHandler);
        cpuThreadPoolExecutor.allowCoreThreadTimeOut(true);
        ioThreadPoolExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
    }

    public static TaskExecutorManager getInstance(){
        return INSTANCE;
    }
}
