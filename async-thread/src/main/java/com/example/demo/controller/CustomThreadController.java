package com.example.demo.controller;

import com.example.demo.async.CustomAsyncTask;
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
@RequestMapping("/customThread")
public class CustomThreadController {

    /*@Autowired
    private CustomAsyncTask customAsyncTask;

    @GetMapping("/test")
    public void test() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            customAsyncTask.test(i);
        }
    }*/
}
