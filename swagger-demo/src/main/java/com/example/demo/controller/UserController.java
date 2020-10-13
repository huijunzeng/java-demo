package com.example.demo.controller;

import com.example.demo.dto.UserSaveDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:01
 */

@RestController
@RequestMapping("/user")
@Api(value = "user", tags = {"用户管理restful接口"})
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "新增用户", notes = "新增用户(表单提交，paramType设置为form，参数注解@ModelAttribute可省略)")
    @ApiImplicitParam(paramType = "body", name = "userSaveDTO", value = "新增用户参数", required = true, dataTypeClass = UserSaveDTO.class)
    @PostMapping("/save")
    public boolean save(@RequestBody UserSaveDTO userSaveDTO) {
        return userService.save(userSaveDTO);
    }

    @ApiOperation(value = "更新用户", notes = "更新用户(json提交，paramType设置为body，参数注解@RequestBody不可省略)")
    @ApiImplicitParam(paramType = "body", name = "userDTO", value = "更新用户参数", required = true, dataTypeClass = UserUpdateDTO.class)
    @PostMapping("/update")
    public boolean update(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.update(userUpdateDTO);
    }

    @ApiOperation(value = "获取用户", notes = "根据用户名获取指定用户信息(url请求参数，paramType设置为query，参数注解@RequestParam不可省略)")
    @ApiImplicitParam(paramType = "query", name = "name", value = "用户名", required = true, dataTypeClass = String.class)
    @GetMapping("/get")
    public UserVO get(@RequestParam String name) {
        return userService.getByName(name);
    }

    @ApiOperation(value = "删除用户", notes = "根据用户id删除用户(url路径参数，paramType设置为path，参数注解@PathVariable不可省略)")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户id", required = true, dataTypeClass = Long.class)
    @DeleteMapping("/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    @ApiOperation(value = "从请求头获取token信息", notes = "从请求头获取token信息(url头部参数，paramType设置为header，参数注解@RequestHeader不可省略)")
    @ApiImplicitParam(paramType = "header", name = "token", value = "token令牌", required = false, dataTypeClass = String.class)
    @GetMapping("/token")
    public String remove(@RequestHeader("token") String token) {
        return token;
    }
}
