package com.example.demo.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.constants.JwtConstants;
import com.example.demo.constants.RedisCacheConstants;
import com.example.demo.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zjh
 * @Description
 * @date 2020/12/07 13:38
 */

@Component
@Slf4j
public class JwtTokenUtil {

    // 密钥key
    @Value("${jwt.secretKey}")
    private static final String SECRET_KEY = "test123456";
    // 一天有效期
    @Value("${jwt.expireTime}")
    private static final long EXPIRE_TIME = 864000000L;
    private static final String ISSUER = "cof";

    /**静态类 构造注入*/
    private static RedisUtil redisUtil;
    @Autowired
    public JwtTokenUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 签发token
     */
    public static String createToken(UserDetails userDetails) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(EXPIRE_TIME / 1000);
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expireTimeDate = Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Map<String, Object> headerClaims = new HashMap<String, Object>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");
        // 签名
        String token = JWT.create()
                .withHeader(headerClaims)
                .withIssuer(ISSUER)
                .withIssuedAt(nowDate)
                .withExpiresAt(expireTimeDate)
                .withNotBefore(nowDate)
                .withSubject("jwt")
                // 自定义的负载信息--用户名name 用户角色--roles
                .withClaim(JwtConstants.CLAIM_USERNAME, userDetails.getUsername())
                .withClaim(JwtConstants.CLAIM_ROLES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        //redis缓存
        redisUtil.set(RedisCacheConstants.TOKEN_CACHE_KEY + userDetails.getUsername(), token);
        return token;
    }

    /**
     * 解签token
     */
    public static DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        DecodedJWT verify = verifier.verify(token);
        return verify;
    }

    /**
     *清除token （客户端需要清除token）
     */
    public static void removeToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        String username = decodedJWT.getClaim(JwtConstants.CLAIM_USERNAME).asString();
        //清除redis缓存
        redisUtil.del(RedisCacheConstants.TOKEN_CACHE_KEY + username);
    }

    public static void main(String[] args) {
        String authHeader  = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqd3QiLCJuYmYiOjE2MDczOTMxMjksInJvbGVzIjpbImEwMDEiXSwiaXNzIjoiY29mIiwiZXhwIjoxNjA3Mzk3NDQ5LCJpYXQiOjE2MDczOTMxMjksInVzZXJuYW1lIjoidGVzdCJ9.YDzxuGePBB0sAUeBaYK2V4naP339FyT4yNi7Ks0xA5U";
        final String BEARER = "Bearer ";
        String token = StringUtils.substring(authHeader, BEARER.length());
        DecodedJWT decodedJWT = JwtTokenUtil.verifyToken(token);
        String username = decodedJWT.getClaim(JwtConstants.CLAIM_USERNAME).asString();
        System.out.println(username);
    }
    /*public static void main(String[] args) throws InterruptedException {
        UserDetails user = new UserDetails();
        user.setPassword("123456");
        user.setUsername("zeng");
        JwtUtil jwtUtil = new JwtUtil();
        String token = jwtUtil.createToken(user);
        System.out.println(token);
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
    }*/
}
