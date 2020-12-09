package com.example.demo.entity;

import com.example.demo.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zjh
 * @Description
 * @date 2020/12/09 11:52
 */

@ApiModel(description = "用户表")
@Data
public class UserEntity extends BaseEntity {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码密文
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;
}
