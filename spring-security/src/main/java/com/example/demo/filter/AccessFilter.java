package com.example.demo.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.constants.JwtConstants;
import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ExceptionTypeEnums;
import com.example.demo.utils.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * @author zjh
 * @Description token鉴权过滤器
 * @date 2020/12/07 14:10
 */
@Slf4j
//@Configuration
public class AccessFilter extends GenericFilterBean {

    @Value("${ignore.urls}")
    private String IGNORE_URLS = "/api/v1/user/login";

    /**
     * @RestControllerAdvice 全局异常类只捕获controller控制层的异常，而filter过滤器在请求到达controller之前执行，所以针对filter过滤器的异常，需要通过HandlerExceptionResolver处理
     * */
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(final ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = StringUtils.substring(authHeader, JwtConstants.BEARER.length());
        if (Stream.of(IGNORE_URLS.split(",")).anyMatch(ignoreUrl -> request.getRequestURI().startsWith(StringUtils.trim(ignoreUrl)))) {
            log.info("ignore url : " + request.getRequestURI());
            chain.doFilter(req, res);
        } else if (StringUtils.isBlank(token)) {
            handlerExceptionResolver.resolveException(request, response, null, new BusinessException(ExceptionTypeEnums.NO_TOKEN_EXCEPTION));
            return;
        } else {
            try {
                DecodedJWT claims = JwtTokenUtil.verifyToken(token);
            } catch (Exception e) {
                e.printStackTrace();
                handlerExceptionResolver.resolveException(request, response, null, e);
                return;
            }
            //鉴权 todo
           chain.doFilter(req, res);
        }
    }
}
