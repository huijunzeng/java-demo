package com.example.demo.response;

import com.example.demo.exception.ExceptionTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.stream.Stream;

/**
 * @Author: ZJH
 * @Date: 2020/12/8 09:32
 */

@RestControllerAdvice
@Slf4j
public class BaseGlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private String IGNORE_URLS = "/swagger-ui.html,/swagger/,/swagger-resources,/static/,/doc.html,/v2/api-docs,/favor.ioc";

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
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
