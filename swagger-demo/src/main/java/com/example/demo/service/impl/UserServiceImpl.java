package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.UserSaveDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 18:01
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(UserSaveDTO userSaveDTO) {
        UserEntity userEntity = BeanUtils.instantiateClass(UserEntity.class);
        BeanUtils.copyProperties(userSaveDTO, userEntity);
        return super.save(userEntity);
    }

    @Override
    public boolean update(UserUpdateDTO userUpdateDTO) {
        UserEntity userEntity = super.getById(userUpdateDTO.getId());
        BeanUtils.copyProperties(userUpdateDTO, userEntity);
        return super.updateById(userEntity);
    }

    @Override
    public UserVO getByName(String name) {
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserEntity::getName, name);
        UserEntity userEntity = super.getOne(queryWrapper);
        UserVO userVO = BeanUtils.instantiateClass(UserVO.class);
        BeanUtils.copyProperties(userEntity, userVO);
        return userVO;
    }

    @Override
    public int count() {
        return super.count();
    }
}
