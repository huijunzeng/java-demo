package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author zjh
 * @Description Spring Security配置
 * @date 2020/12/07 15:05
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${ignore.urls}")
    private String IGNORE_URLS = "/api/v1/user/login";

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private CustomAuthExceptionHandler customAuthExceptionHandler;

    /**
     * 认证对象管理
     * springboot2.1.x版本需要注入父类的authenticationManager  不然AuthorizationServerConfig授权服务器依赖AuthenticationManager报错
     * Field authenticationManager in com.xxx.xxx required a bean of type 'org.springframework.security.authentication.AuthenticationManager' that could not be found.
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * PasswordEncoder是对密码的加密处理，如果数据库中的user密码没有加密，则可以不加此NoOpPasswordEncoder方法。注意加密请使用security自带的加密方式。
     * PasswordEncoder是对密码的加密处理，如果数据库中的user密码加密，则需奥使用匹配的加密方法，比如BCryptPasswordEncoder
     */
    //@Bean
    //public PasswordEncoder passwordEncoder() {
    //    return new BCryptPasswordEncoder();
    //}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 认证对象管理构造器  用于管理用户
     * @param builder
     * @throws Exception
     */
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 假如不设置false，当myUserDetailsService类的loadUserByUsername方法查询不到用户时抛出的UsernameNotFoundException异常会被跳过，转换抛出BadCredentialsException异常
        // 为了区别具体的错误，找不到该username的用户时应该抛出UsernameNotFoundException异常；密码不匹配则抛出BadCredentialsException异常
        // 源码AbstractUserDetailsAuthenticationProvider类中的Authentication authenticate(Authentication authentication)方法的第68行
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        builder.authenticationProvider(daoAuthenticationProvider);
    }

    /**
     * Web层面的配置，一般用来配置无需权限校验的路径，也可以在HttpSecurity中配置，但是在web.ignoring()中配置效率更高。
     * web.ignoring()是一个忽略的过滤器，而HttpSecurity中定义了一个过滤器链，即使permitAll()放行还是会走所有的过滤器，
     * 直到最后一个过滤器FilterSecurityInterceptor认定是可以放行的，才能访问。
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    /**
     * 资源授权
     * 与http安全配置相关 可配置拦截什么URL、设置什么权限等安全控制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] ignoreUrls = IGNORE_URLS.split(",");
        // 配置对某些访问路径放行(不需要携带token即可访问)
        http.csrf().disable()//禁用了 csrf 功能

                //配置资源访问权限  依次顺序进行匹配
                .authorizeRequests()
                .antMatchers(ignoreUrls).permitAll()//放行请求路径
                .antMatchers("/admin/**").hasRole("admin")//请求路径以/admin/开头的，必须需要拥有admin角色才能访问
                .antMatchers("/user/**").hasAnyRole("admin", "user")//请求路径以/user/开头的，只需要拥有admin或user角色的其一就能访问
                .anyRequest().permitAll()//剩下的其他请求路径都可以访问
                //.anyRequest().authenticated()//剩下的其他请求路径，需要登录后才能访问
                //.and().anonymous()//对于没有配置权限的其他请求允许匿名访问
                // 允许配置错误处理(AccessDeniedHandler:用来解决认证过的用户访问无权限资源时的异常  AuthenticationEntryPoint:用来解决匿名用户访问无权限资源时的异常)
                .and().exceptionHandling().accessDeniedHandler(customAuthExceptionHandler).authenticationEntryPoint(customAuthExceptionHandler)
                
                //.and().formLogin()//使用 spring security 默认登录页面
                .and().httpBasic();//启用http 基础验证
    }
}
