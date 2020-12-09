package com.example.demo.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理类  按顺序优先处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * AccessDeniedException 拒绝访问异常
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseVO badMethodExpressException(AccessDeniedException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage());
        return ResponseVO.fail(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    /**
     * TokenExpiredException token过期
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({TokenExpiredException.class})
    public ResponseVO tokenExpiredException(TokenExpiredException ex) {
        log.error("token has expired:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.TOKEN_HAS_EXPIRED);
    }

    /**
     * JWTDecodeException token解析异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({JWTDecodeException.class})
    public ResponseVO jwtDecodeException(JWTDecodeException ex) {
        log.error("JWT decode exception:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.JWT_DECODE_EXCEPTION);
    }

    /**
     * SignatureVerificationException token令牌签名异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({SignatureVerificationException.class})
    public ResponseVO tokenHasExpiredException(SignatureVerificationException ex) {
        log.error("signature verification exception:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.SIGNATURE_VERIFICATION_EXCEPTION);
    }

    /**
     * methodArgumentNotValidException 参数检验异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseVO methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException start, uri:{}, caused by: {} ", request.getRequestURI(), ex);
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            msg.append(", ").append(fieldError.getField()).append(fieldError.getDefaultMessage());
        }
        return ResponseVO.fail(ExceptionTypeEnums.PARAM_VALID_ERROR, msg == null ? ExceptionTypeEnums.PARAM_VALID_ERROR.getMsg() : msg.substring(2));
    }

    /**
     * missingServletRequestParameterException 参数绑定异常
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseVO missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("missing servlet request parameter exception:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.PARAM_BIND_ERROR);
    }

    /**
     * noHandlerFoundException 请求找不到异常
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseVO noHandlerFoundException(NoHandlerFoundException ex) {
        log.error("httpRequestMethodNotSupportedException:{}", ex.getMessage());
        return ResponseVO.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    /**
     * HttpRequestMethodNotSupportedException 方法请求类型异常
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseVO httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("httpRequestMethodNotSupportedException:{}", ex.getMessage());
        return ResponseVO.fail(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase());
    }

    /**
     * DuplicateKeyException 唯一键冲突
     */
    @ExceptionHandler(value = {DuplicateKeyException.class})
    public ResponseVO duplicateKeyException(DuplicateKeyException ex) {
        log.error("primary key duplication exception:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.DUPLICATE_PRIMARY_KEY);
    }

    /**
     * ArithmeticException 算法异常
     */
    @ExceptionHandler(value = {ArithmeticException.class})
    public ResponseVO arithmeticException(ArithmeticException ex) {
        log.error("arithmeticException:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.ARITHMETIC_ERROR);
    }

    /**
     * IllegalArgumentException 非法参数
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseVO illegalArgumentException(IllegalArgumentException ex) {
        log.error("illegalArgumentException:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.PARAM_VALID_ERROR, ex.getMessage());
    }

    /**
     * SQLException sql异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public ResponseVO SQLException(SQLException ex) {
        log.error("SQLException:{}", ex.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.PARAM_VALID_ERROR, ex.getMessage());
    }

    /**
     * ConstraintViolationException 单个参数校验
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseVO constraintViolationException(ConstraintViolationException ex){
        log.error("constraintViolationException:{}", ex.getMessage());
        StringBuilder msg = new StringBuilder();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation constraintViolation : constraintViolations
             ) {
            msg.append(", ").append(constraintViolation.getMessage());
        }
        return ResponseVO.fail(ExceptionTypeEnums.PARAM_VALID_ERROR, msg == null ? ExceptionTypeEnums.PARAM_VALID_ERROR.getMsg() : msg.substring(2));
    }

    /**
     * BusinessException 业务异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public ResponseVO businessException(BusinessException ex) {
        log.error("businessException:{}", ex.getMessage());
        return ResponseVO.fail(ex.getCode(), ex.getMessage());
    }

    /**
     * 其他未知异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseVO exception(Exception ex) {
        log.error("exception:{}", ex);
        return ResponseVO.fail(ExceptionTypeEnums.SERVER_INTERNAL_ERROR);
    }

    /**
     * 其他未知异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Throwable.class})
    public ResponseVO throwable(Throwable throwable) {
        log.error("throwable:{}", throwable.getMessage());
        return ResponseVO.fail(ExceptionTypeEnums.SERVER_INTERNAL_ERROR);
    }

}