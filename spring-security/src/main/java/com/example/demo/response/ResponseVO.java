package com.example.demo.response;

import com.example.demo.exception.ExceptionType;
import com.example.demo.exception.ExceptionTypeEnums;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenws
 * @decription
 * @date 2018/8/2
 */
@Data
public class ResponseVO<T> implements Serializable {

    private static final long serialVersionUID = 6775422262797117144L;

    @ApiModelProperty("返回编码，0表示成功，其他表示失败")
    private Integer code;

    @ApiModelProperty("错误信息")
    private String message;

    @ApiModelProperty("数据")
    private T data;


    public ResponseVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @param exceptionType 业务操作响应码
     */
    public ResponseVO(ExceptionType exceptionType) {
        this(exceptionType.getCode(), exceptionType.getMsg(), null);
    }

    public ResponseVO(ExceptionType exceptionType, T data) {
        this(exceptionType.getCode(), exceptionType.getMsg(), data);
    }

    public ResponseVO(ExceptionType exceptionType, String msg) {
        this(exceptionType.getCode(), msg);
    }

    public ResponseVO(ExceptionType exceptionType, String msg, T data) {
        this(exceptionType.getCode(), msg, data);
    }

    public ResponseVO(Integer code, String msg) {
        this(code, msg, null);
    }

    public static <T> ResponseVO<T> success(Integer code, String msg, T data) {
        return new ResponseVO<>(code, msg, data);
    }

    public static <T> ResponseVO<T> success(String msg, T data) {
        return new ResponseVO<>(ExceptionTypeEnums.SUCCESS, msg, data);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(ExceptionTypeEnums.SUCCESS, data);
    }


    public static <T> ResponseVO<T> success(ExceptionType exceptionType) {
        return new ResponseVO<>(exceptionType);
    }

    public static <T> ResponseVO<T> success(String msg) {
        return new ResponseVO<>(ExceptionTypeEnums.SUCCESS, msg);
    }

    public static <T> ResponseVO<T> fail(ExceptionType exceptionType) {
        return new ResponseVO<>(exceptionType);
    }

    public static <T> ResponseVO<T> fail(ExceptionType exceptionType, String msg) {
        return new ResponseVO<>(exceptionType.getCode(), msg);
    }

    public static <T> ResponseVO<T> fail(String msg) {
        return new ResponseVO<>(ExceptionTypeEnums.FAIL, msg);
    }

    public static <T> ResponseVO<T> fail(Integer code, String msg) {
        return new ResponseVO<>(code, msg);
    }

    /**
     * @param flag 成功状态
     * @param <T>  T 泛型标记
     * @return
     */
    public static <T> ResponseVO<T> status(boolean flag) {
        return flag ? success(ExceptionTypeEnums.SUCCESS) : fail(ExceptionTypeEnums.FAIL);
    }

}
