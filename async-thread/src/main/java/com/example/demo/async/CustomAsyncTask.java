package com.example.demo.async;

import com.example.demo.service.ICustomThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zjh
 * @Description
 * @date 2020/12/28 16:05
 */

@Component
public class CustomAsyncTask {

    @Autowired
    private ICustomThreadService iCustomThreadService;

    @Async("executor") // 使用自定义线程池时，需要标明配置的Bean的名称
    public void test(int index) throws InterruptedException {
        Thread.sleep(1000L);
        String threadName = Thread.currentThread().getName();
        iCustomThreadService.test(threadName + "===" + index);
    }
}
