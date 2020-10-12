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

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @ApiImplicitParam(paramType = "body", name = "userSaveDTO", value = "新增用户参数", required = true, dataTypeClass = UserSaveDTO.class)
    @PostMapping("/save")
    public boolean save(@RequestBody UserSaveDTO userSaveDTO) {
        return userService.save(userSaveDTO);
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @ApiImplicitParam(paramType = "body", name = "userDTO", value = "更新用户参数", required = true, dataTypeClass = UserSaveDTO.class)
    @PostMapping("/update")
    public boolean update(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.update(userUpdateDTO);
    }

    @ApiOperation(value = "获取用户", notes = "根据用户名获取指定用户信息")
    @ApiImplicitParam(paramType = "query", name = "name", value = "用户名", required = true, dataTypeClass = String.class)
    @GetMapping("/get")
    public UserVO get(@RequestParam String name) {
        return userService.getByName(name);
    }

    @ApiOperation(value = "删除用户", notes = "根据用户id删除用户")
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户id", required = true, dataTypeClass = Long.class)
    @DeleteMapping("/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }
}
