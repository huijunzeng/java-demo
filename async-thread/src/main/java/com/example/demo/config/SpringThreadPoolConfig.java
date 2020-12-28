package com.example.demo.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zjh
 * @Description 使用spring封装的线程池，需要实现AsyncConfigurer接口
 * @date 2020/12/28 15:06
 */

@Configuration
public class SpringThreadPoolConfig implements AsyncConfigurer {


    @Value("${thread.pool.core-pool-size}")
    private int corePoolSize;

    @Value("${thread.pool.max-pool-size}")
    private int maxPoolSize;

    @Value("${thread.pool.queue-capacity}")
    private int queueCapacity;

    @Value("${thread.pool.keep-alive-seconds}")
    private int keepAliveSeconds;

    @Value("${thread.pool.thread-name-prefix}")
    private String threadNamePrefix;

    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);// 线程池维护线程的最小数量
        executor.setMaxPoolSize(maxPoolSize);// 线程池维护线程的最大数量
        executor.setQueueCapacity(queueCapacity);//  线程池所使用的缓冲队列
        executor.setKeepAliveSeconds(keepAliveSeconds);// 空闲线程的存活时间
        executor.setThreadNamePrefix(threadNamePrefix + "spring-");// 线程名称前缀
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 使用spring封装的线程池，必须初始化，不然报错
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
