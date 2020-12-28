package com.example.demo.service;

import com.example.demo.vo.UserLoginVO;

/**
 * @author zjh
 * @Description
 * @date 2020/12/09 17:03
 */
public interface IUserService {

    UserLoginVO login(String username, String password);

    Boolean logout(String token);
}
