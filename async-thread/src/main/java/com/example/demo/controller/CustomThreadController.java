package com.example.demo.controller;

import com.example.demo.async.CustomAsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

/**
 * @author zjh
 * @Description
 * @date 2020/12/28 16:29
 */
@RestController
@RequestMapping("/customThread")
public class CustomThreadController {

    @Autowired
    private CustomAsyncTask customAsyncTask;

    @Autowired
    @Qualifier("springThreadPool")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 直接使用线程池执行
     * @throws InterruptedException
     */
    @GetMapping("/origin")
    public void origin() throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("=======" + Thread.currentThread().getName());
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("total time: " + (end-start));
    }

    /**
     * 结合@Async简化使用
     * @throws InterruptedException
     */
    @GetMapping("/springThreadPool")
    public void test() throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            customAsyncTask.springThreadPool(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("total time=======" + (end - start));
    }

    /**
     * 结合@Async简化使用
     * @throws InterruptedException
     */
    @GetMapping("/customThreadPool")
    public void customThreadPool() throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            customAsyncTask.customThreadPool(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("total time=======" + (end - start));
    }

    /**
     * 结合@Async简化使用
     * @throws InterruptedException
     */
    @GetMapping("/cachedThreadPool")
    public void cachedThreadPool() throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            customAsyncTask.cachedThreadPool(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("total time=======" + (end - start));
    }
}
