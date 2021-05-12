package com.sand.core.thread.pool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能说明：线程池使用 <br>
 * 开发人员：hsh <br>
 * 开发时间：2021/5/12 16:21 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class ThreadPoolHandler {
    /**
     * 线程池
     */
    private ExecutorService threadPool;

    /**
     * 线程任务
     */
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * <p>
     * 功能描述：构造函数
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:33
     * 修改记录：新建
     */
    public ThreadPoolHandler() {
        init();
        initExecutor();
    }

    /**
     * <p>
     * 功能描述：初始化
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:32
     * 修改记录：新建
     */
    public void init() {
        // 核心线程池大小 = CPU核数
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        // 最大线程池大小 = CPU核数 * 2
        int maximumPoolSize = corePoolSize * 2;
        // 线程池中超过 corePoolSize 数目的空闲线程最大存活时间
        long keepAliveTime = 60;
        // 阻塞任务队列最大容量 1024
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(1024);
        System.out.println("corePoolSize=" + corePoolSize + "，maximumPoolSize=" + maximumPoolSize);
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, new ThreadPoolExecutor.CallerRunsPolicy());
        System.out.println("threadPool 初始化完成");
    }

    /**
     * <p>
     * 功能描述：初始化
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:32
     * 修改记录：新建
     */
    public void initExecutor() {
        if (null == taskExecutor) {
            taskExecutor = new ThreadPoolTaskExecutor();
        }
        // CPU核数
        int cpuNum = Runtime.getRuntime().availableProcessors();
        // 核心线程大小
        taskExecutor.setCorePoolSize(cpuNum);
        // 最大线程大小
        taskExecutor.setMaxPoolSize(cpuNum * 2);
        // 队列最大容量
        taskExecutor.setQueueCapacity(500);
        // 当提交的任务个数大于 QueueCapacity，就需要设置该参数，
        // 但spring提供的都不太满足业务场景，可以自定义一个，也可以注意不要超过 QueueCapacity 即可
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池中超过 corePoolSize 数目的空闲线程最大存活时间
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.setThreadNamePrefix("Thread-Pool-Task-Executor-");
        taskExecutor.initialize();
        System.out.println("taskExecutor 初始化完成");
    }

    /**
     * <p>
     * 功能描述：线程池使用
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:19
     * 修改记录：新建
     */
    public void useThreadPool() {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> testMap = new HashMap<>(4);
        dataList.add(testMap);
        // 分批处理数据
        int threadSize;
        int batchSize = 1000;
        int dataSize = dataList.size();
        if (dataSize % batchSize == 0) {
            threadSize = dataSize / batchSize;
        } else {
            threadSize = dataSize / batchSize + 1;
        }
        List<Map<String, Object>> batchDataList;
        for (int i = 0; i < threadSize; i++) {
            if (i == threadSize - 1) {
                batchDataList = dataList.subList(i * batchSize, dataSize);
            } else {
                batchDataList = dataList.subList(i * batchSize, (i + 1) * batchSize);
            }
            final List<Map<String, Object>> tempDataList = batchDataList;
            Runnable task = new Runnable() {

                @Override
                public void run() {
                    try {
                        if (tempDataList.size() > 0) {
                            for (Map<String, Object> data : tempDataList) {
                                // TODO 需要处理的业务
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("分批处理数据异常：" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            };
            threadPool.submit(task);
        }
        threadPool.shutdown();
    }

    /**
     * <p>
     * 功能描述：信息汇总
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:26
     * 修改记录：新建
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     */
    public Map<String, Object> getDataInfo() {
        Future<Map<String, Object>> baseTask = this.threadPool.submit(new BaseTask());
        Future<Map<String, Object>> otherTask = this.threadPool.submit(new OtherTask());
        Map<String, Object> resultMap = new HashMap<>(4);
        try {
            // 基础信息
            Map<String, Object> baseInfo = baseTask.get();
            resultMap.put("baseInfo", baseInfo);
            // 其他信息
            Map<String, Object> otherInfo = otherTask.get();
            resultMap.put("otherInfo", otherInfo);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            this.threadPool.shutdown();
        }
        return resultMap;
    }

    /**
     * <p>
     * 功能描述：统计数据一（假设是人员基本信息）
     * </p>
     * 开发人员：hsh
     * 开发时间：2021/5/12 17:25
     * 修改记录：新建
     */
    private class BaseTask implements Callable<Map<String, Object>> {
        @Override
        public Map<String, Object> call() throws Exception {
            Map<String, Object> baseInfo = new HashMap<>(4);
            baseInfo.put("username", "张三");
            baseInfo.put("sex", "男");
            baseInfo.put("age", 22);

            return baseInfo;
        }
    }

    /***
     * <p>
     * 功能描述：统计数据一（假设是人员其他信息）
     * </p>
     * 开发人员：gy-hsh
     * 开发时间：2021/5/12 17:26
     * 修改记录：新建
     *
     */
    private class OtherTask implements Callable<Map<String, Object>> {
        @Override
        public Map<String, Object> call() throws Exception {
            Map<String, Object> otherInfo = new HashMap<>(4);
            otherInfo.put("hobby", "徒步旅行");
            otherInfo.put("specialty", "Java");

            return otherInfo;
        }
    }

    public static void main(String[] args) throws Exception {
        ThreadPoolHandler threadPoolHandler = new ThreadPoolHandler();
        Map<String, Object> resultMap = threadPoolHandler.getDataInfo();
        System.out.println("resultMap：" + resultMap);

        // 以下案例只有JDK8才支持
//        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
//        String jvmName = runtimeBean.getName();
//        System.out.println("JVM Name = " + jvmName);
//        long pid = Long.parseLong(jvmName.split("@")[0]);
//        System.out.println("JVM PID  = " + pid);
//        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//        int n = 300;
//        for (int i = 0; i < n; i++) {
//            ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 1000, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024));
//            for (int j = 0; j < 10; j++) {
//                executor.execute(() -> {
//                    System.out.println("当前线程总数为：" + bean.getThreadCount());
//                });
//            }
//        }
//        Thread.sleep(5000);
//        System.out.println("线程总数为 = " + bean.getThreadCount());
    }

}