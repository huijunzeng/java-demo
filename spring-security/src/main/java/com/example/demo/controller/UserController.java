package com.example.demo.controller;

import com.example.demo.constants.JwtConstants;
import com.example.demo.constants.RedisCacheConstants;
import com.example.demo.service.IUserService;
import com.example.demo.utils.redis.RedisUtil;
import com.example.demo.vo.UserLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@Validated
public class UserController {

	@Autowired
	private IUserService iUserService;

	@ApiOperation(value = "登录接口", httpMethod = "GET")
	@GetMapping("/login")
	public UserLoginVO login(@RequestParam String username, @RequestParam String password) {
		return iUserService.login(username, password);
	}

	@ApiOperation(value = "登出接口", httpMethod = "GET")
	@GetMapping("/logout")
	public Boolean logout(HttpServletRequest request){
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = StringUtils.substring(authHeader, JwtConstants.BEARER.length());
		iUserService.logout(token);
		return iUserService.logout(token);
	}

}
