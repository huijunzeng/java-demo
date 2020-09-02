package com.example.dynamicdatasource.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zjh
 * @Description
 * @date 2020/09/02 11:30
 */
@ApiModel(description = "用户VO")
@Data
public class UserVO {

    @ApiModelProperty("用户名")
    private String name;
}
