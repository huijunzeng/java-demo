package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserSaveDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.vo.UserVO;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:00
 */
public interface UserService extends IService<UserEntity> {

    boolean save(UserSaveDTO userSaveDTO);

    boolean update(UserUpdateDTO userUpdateDTO);

    UserVO getByName(String name);

    int count();
}
