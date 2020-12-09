package com.example.demo.exception;

import lombok.Data;

/**
 * @author zjh
 * @Description 业务异常类
 * @date 2020/12/07 14:10
 */
@Data
public class BusinessException extends RuntimeException {

	/**
	 * 错误类型码
	 */
	private Integer code;

	public BusinessException(ExceptionType exceptionType) {
		super(exceptionType.getMsg());
		this.code = exceptionType.getCode();
	}

	public BusinessException(Integer code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Integer code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
}

