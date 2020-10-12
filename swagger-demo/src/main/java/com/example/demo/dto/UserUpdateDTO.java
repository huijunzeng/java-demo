package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zjh
 * @Description 用户更新DTO
 * @date 2020/09/02 11:18
 */

@ApiModel(description = "用户更新DTO")
@Data
public class UserUpdateDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户名")
    private String name;
}
