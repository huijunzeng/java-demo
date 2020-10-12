package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/save1")
    public boolean save1(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @PostMapping("/save2")
    public boolean save2(@RequestBody UserDTO userDTO) {
        return userService.save(userDTO);
    }

    @GetMapping("/get1")
    public UserVO get1(@RequestParam String name) {
        return userService.getByName(name);
    }

    @GetMapping("/get2")
    public UserVO get2(@RequestParam String name) {
        return userService.getByName(name);
    }

    @GetMapping("/count1")
    public int count1() {
        return userService.count();
    }

    @GetMapping("/count2")
    public int count2() {
        return userService.count();
    }
}
