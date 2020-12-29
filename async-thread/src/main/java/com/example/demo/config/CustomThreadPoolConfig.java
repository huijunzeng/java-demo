package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author zjh
 * @Description 自定义线程池
 * @date 2020/12/28 15:06
 */

@Configuration
public class CustomThreadPoolConfig {

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

    @Bean("executor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);// 线程池维护线程的最小数量  最佳线程个数，cpu处理器核数*2
        executor.setMaxPoolSize(maxPoolSize);// 线程池维护线程的最大数量
        executor.setQueueCapacity(queueCapacity);//  线程池所使用的缓冲队列
        executor.setKeepAliveSeconds(keepAliveSeconds);// 空闲线程的存活时间
        executor.setThreadNamePrefix(threadNamePrefix + "custom-");// 线程名称前缀
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}
