package com.deer.base.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class JwtUtils {

    private final String secret;
    private final Long expiration;
    private final String header;
    private final String tokenPrefix;

    /**
     * 生成JWT令牌
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("created", new Date());
        
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 验证令牌是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.setIssuedAt(new Date());
        return generateToken(claims);
    }

    // 私有辅助方法
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}