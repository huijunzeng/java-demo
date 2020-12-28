package com.example.demo.controller;

import com.example.demo.async.SpringAsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjh
 * @Description
 * @date 2020/12/28 16:29
 */
@RestController
@RequestMapping("/springThread")
public class SpringThreadController {

    @Autowired
    private SpringAsyncTask springAsyncTask;

    @GetMapping("/test")
    public void test() throws InterruptedException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            springAsyncTask.test(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("total time=======" + (end - start));
    }
}
