package com.example.demo.config;

import com.example.demo.exception.ExceptionTypeEnums;
import com.example.demo.response.ResponseVO;
import com.example.demo.tools.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义未授权 token无效 权限不足返回信息处理类
 * 默认返回如同以下的信息：
 * {
 *     "error": "unauthorized",
 *     "error_description": "Full authentication is required to access this resource"
 * }
 * 需要改成统一异常类的格式：
 * {
 *     "code": 500,
 *     "msg": "内部服务错误"
 * }
 * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
 * AccessDeniedHandler 用来解决认证过的用户访问无权限资源时的异常
 * @Author: ZJH
 * @Date: 2020/11/19 17:39
 */

@Component
@Slf4j
public class CustomAuthExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    /**
     * AuthenticationEntryPoint
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Throwable cause = authException.getCause();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // CORS "pre-flight" request
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control","no-cache");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.addHeader("Access-Control-Max-Age", "1800");
        /*if (cause instanceof InvalidTokenException) {
            log.error("InvalidTokenException : {}",cause.getMessage());
            // Token无效
            response.getWriter().write(JSONUtil.objectToJson(ResponseVO.fail(ExceptionTypeEnums.INVALID_TOKEN)));
        } else {*/
            log.error("Unauthorized : Unauthorized");
            // 资源未授权
            response.getWriter().write(JSONUtil.objectToJson(ResponseVO.fail(ExceptionTypeEnums.UN_AUTHORIZED)));
        //}
    }

    /**
     * AccessDeniedHandler
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Cache-Control","no-cache");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.addHeader("Access-Control-Max-Age", "1800");
        //访问资源的用户权限不足
        log.error("AccessDeniedException : {}", accessDeniedException.getMessage());
        response.getWriter().write(JSONUtil.objectToJson(ResponseVO.fail(401, accessDeniedException.getMessage())));
    }
}
