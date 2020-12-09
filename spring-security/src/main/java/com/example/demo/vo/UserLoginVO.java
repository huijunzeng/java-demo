package com.example.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjh
 * @Description
 * @date 2020/12/09 16:59
 */

@ApiModel(value = "用户登录", description = "用户登录")
@Data
public class UserLoginVO {

    @ApiModelProperty("token")
    private String token;

}
