package com.example.demo.exception;

/**
 * @author zjh
 * @Description 异常类型接口
 * @date 2020/12/07 14:10
 */
public interface ExceptionType {
    Integer getCode();

    String getMsg();

    Integer getHttpCode();
}
