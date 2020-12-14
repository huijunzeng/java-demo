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
 * @Description  token 工具类
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
     * token： eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqd3QiLCJuYmYiOjE2MDc5MjQ0NDAsInJvbGVzIjpbIui2hee6p-euoeeQhuWRmCJdLCJpc3MiOiJjb2YiLCJleHAiOjE2MDg3ODg0NDAsImlhdCI6MTYwNzkyNDQ0MCwidXNlcm5hbWUiOiJ0ZXN0MTIifQ.2iM-i6-4SJDKu-V0gyL_nribFSQxksZoQb8TlpAHwtg
     * 一个token有完整的三部分构成，使用.隔开
     * 第一部分为Header头部信息，第二部分为Payload负载信息，这两部分均由MD5加密而成；第三部分为签名信息（即第一部分加上第二部分，再根据提供的secret密钥签名而成）
     */
    public static String createToken(UserDetails userDetails) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(EXPIRE_TIME / 1000);
        Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expireTimeDate = Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        Map<String, Object> headerClaims = new HashMap<String, Object>();
        // JWT Header信息 应该存放什么，可参考官网文档
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
                // 自定义的Payload负载信息，比如签发时间、过期时间、用户名name、用户角色等
                .withClaim(JwtConstants.CLAIM_USERNAME, userDetails.getUsername())
                .withClaim(JwtConstants.CLAIM_ROLES, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        //redis缓存
        redisUtil.set(RedisCacheConstants.TOKEN_CACHE_KEY + userDetails.getUsername(), token);
        return token;
    }

    /**
     * 解签token
     * 注意：解密失败会抛出相关异常，比如TokenExpiredException token过期、JWTDecodeException token解析异常、SignatureVerificationException token令牌签名异常等，我们只需要在全局异常处理类中去捕获异常即可
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
     * 清除token （因为jwt是无状态的，不能去改变token的过期时间，所以客户端也需要清除token）
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
