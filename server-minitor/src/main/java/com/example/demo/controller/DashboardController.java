package com.example.demo.controller;

import com.example.demo.service.SystemMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjh
 * @Description
 * @date 2020/12/04 18:49
 */

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private SystemMonitor systemMonitor;

    @GetMapping("/top")
    public void top() throws Exception {
        systemMonitor.init();
    }
}
