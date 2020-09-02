package com.example.dynamicdatasource.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zjh
 * @Description
 * @date 2020/08/22 17:52
 */

@TableName("user")
@ApiModel(description = "用户表")
@Data
public class UserEntity implements Serializable {

    /**
     * 用户名
     */
    @TableField("name")
    private String name;
}
