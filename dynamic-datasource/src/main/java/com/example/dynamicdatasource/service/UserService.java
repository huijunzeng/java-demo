package com.example.dynamicdatasource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dynamicdatasource.entity.UserEntity;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:00
 */
public interface UserService extends IService<UserEntity> {

    UserEntity getByName(String name);

    int count();
}
