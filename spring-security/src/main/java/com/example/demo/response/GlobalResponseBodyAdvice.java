package com.example.demo.response;

import com.example.demo.exception.ExceptionTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

/**
 * @Author: ZJH
 * @Description 全局响应处理类  以{"code":0,"message":"请求处理成功","data":{"token":"eyJ0eXAiOF"}}结构返回
 * @Date: 2020/12/8 09:32
 */
@RestControllerAdvice
@Slf4j
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    // 不处理直接返回的资源
    private String IGNORE_URLS = "/swagger-ui/,/swagger/,/swagger-resources,/static/,/doc.html,/v3/api-docs,/favor.ioc";

    /**用于判断是否需要做处理  返回false表示不需要做处理*/
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        Method method = methodParameter.getMethod();
        // 例子
        if (method.getName().equals("======")) {
            return false;
        }
        return true;
    }

    /**
     * 对于返回的对象如果不是最终对象ResponseVO，则需要包装一下
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        String path = serverHttpRequest.getURI().getPath();
        if (Stream.of(IGNORE_URLS.split(",")).anyMatch(ignoreUrl -> path.startsWith(StringUtils.trim(ignoreUrl)))) {
            return o;
        }
        if(!(o instanceof ResponseVO)) {
            ResponseVO responseVO = new ResponseVO(ExceptionTypeEnums.SUCCESS, o);
            return responseVO;
        }
        return o;
    }
}
