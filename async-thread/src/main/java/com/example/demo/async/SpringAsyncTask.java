package com.example.demo.async;

import com.example.demo.service.ISpringThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author zjh
 * @Description
 * @date 2020/12/28 16:05
 */

@Component
public class SpringAsyncTask {

    @Autowired
    private ISpringThreadService iSpringThreadService;

    @Async // 使用spring框架封装的线程池时，不需要标明配置的Bean的名称
    public void test(int index) throws InterruptedException {
        Thread.sleep(1000L);
        String threadName = Thread.currentThread().getName();
        iSpringThreadService.test(threadName + "===" + index);
    }
}
