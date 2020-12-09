package com.example.demo.service.impl;

import com.example.demo.exception.BusinessException;
import com.example.demo.exception.ExceptionTypeEnums;
import com.example.demo.service.IUserService;
import com.example.demo.utils.jwt.JwtTokenUtil;
import com.example.demo.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author zjh
 * @Description
 * @date 2020/12/09 17:04
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserLoginVO login(String username, String password) {

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (UsernameNotFoundException e) {
            throw new BusinessException(ExceptionTypeEnums.NO_EXIST_USER);
        } catch (BadCredentialsException e) {
            throw new BusinessException(ExceptionTypeEnums.PASSWORD_WRONG);
        }
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String token = JwtTokenUtil.createToken(userDetails);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    @Override
    public Boolean logout(String token) {
        // 在登录成功后，Spring Security创建Authentication对象并帮我们放到了SecurityContextHolder上下文中
        // 在请求头添加Authorization的token参数，然后通过SecurityContextHolder获取到Authentication对象的用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return null;
    }
}
