package com.example.demo.config;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * swagger属性封装类
 */
@Data
@Component
@ToString
// 读取application.yml配置文件以base.config.swagger为前缀的同名属性并封装成一个SwaggerProperties Bean类  apiBasePackage字段名可对等apiBasePackage、apibasepackage、api-base-package属性名
@ConfigurationProperties(prefix = "base.config.swagger")
public class SwaggerProperties {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("版本号")
    private String version;

    /**属性多层嵌套处理*/
    @ApiModelProperty("创建人信息  可用于定位该模块的负责人")
    private Contact contact = new Contact();

    @Data
    public class Contact {

        @ApiModelProperty("创建人名字")
        private String name;

        @ApiModelProperty("创建人邮箱")
        private String email;

        @ApiModelProperty("创建人项目地址")
        private String url;
    }


}
