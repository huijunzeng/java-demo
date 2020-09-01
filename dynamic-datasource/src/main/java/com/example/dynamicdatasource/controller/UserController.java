package com.example.dynamicdatasource.controller;

import com.example.dynamicdatasource.annotation.DataSource;
import com.example.dynamicdatasource.annotation.DataSourceType;
import com.example.dynamicdatasource.entity.UserEntity;
import com.example.dynamicdatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:01
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get1")
    @DataSource
    public UserEntity get1(@RequestParam String name) {
        return userService.getByName(name);
    }

    @GetMapping("/get2")
    @DataSource(DataSourceType.SLAVE)
    public UserEntity get2(@RequestParam String name) {
        return userService.getByName(name);
    }

    @GetMapping("/count1")
    @DataSource
    public int count1() {
        return userService.count();
    }

    @GetMapping("/count2")
    @DataSource(DataSourceType.SLAVE)
    public int count2() {
        return userService.count();
    }
}
