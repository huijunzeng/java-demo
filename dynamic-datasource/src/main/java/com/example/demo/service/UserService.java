package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.vo.UserVO;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:00
 */
public interface UserService extends IService<UserEntity> {

    boolean save(UserDTO userDTO);

    UserVO getByName(String name);

    int count();
}
