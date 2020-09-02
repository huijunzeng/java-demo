package com.example.dynamicdatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dynamicdatasource.dto.UserDTO;
import com.example.dynamicdatasource.entity.UserEntity;
import com.example.dynamicdatasource.vo.UserVO;

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
