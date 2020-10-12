package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zjh
 * @Description 用户DTO
 * @date 2020/09/02 11:18
 */

@ApiModel(description = "用户DTO")
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String name;
}
