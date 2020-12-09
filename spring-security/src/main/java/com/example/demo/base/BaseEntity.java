package com.example.demo.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础类
 * @Author: ZJH
 * @Date: 2020/1/14 10:21
 */

@Data
@ApiModel(value = "实体类基础类")
public class BaseEntity implements Serializable {

    /**
     * id主键
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 是否已删除 1已删除 0未删除
     * @TableLogic 逻辑删除，调用delete方法实际执行update
     */
    private Integer deleted;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本  每次操作版本加1
     */
    private Long version;
}
