package com.example.demo.config;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.ExceptionTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zjh
 * @Description Spring Security关键类  实现父类UserDetailsService的loadUserByUsername方法
 * @date 2020/12/07 16:45
 */

@Component
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // todo 根据用户名去数据库查找用户（用户名必须唯一）
        //UserEntity userEntity = iUserService.loadUserByUsername(username);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("test12");
        userEntity.setPassword("123456");
        // UsernameNotFoundException为Spring Security框架定义的异常，当找不到这个username的用户时抛出
        if (userEntity == null) {
            throw new UsernameNotFoundException(ExceptionTypeEnums.NO_EXIST_USER.getMsg());
        }
        return new User(userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, this.obtainGrantedAuthorities(userEntity));

    }

    /**
     * 获得用户的所有角色的权限集合.
     *
     * @param userEntity
     * @return
     */
    protected Set<GrantedAuthority> obtainGrantedAuthorities(UserEntity userEntity) {
        // todo 根据用户名去数据库查找用户对应的角色/资源
        List<String> roles = Arrays.asList("admin");
        log.info("user:{},roles:{}", userEntity.getUsername(), roles);
        if (null == roles || roles.isEmpty()) {
            return new HashSet<>();
        }
        // 用户的所有角色
        return roles.stream().map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toSet());
    }

}
