package com.deer.base.security.filter;

import com.deer.base.common.exception.AuthExceptions;
import com.deer.base.common.utils.RedisUtils;
import com.deer.entities.system.vo.LoginUser;
import com.deer.base.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.deer.base.common.constants.RedisConstants.LOGIN_USER;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, RedisUtils redisUtils) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        // 从请求头获取令牌
        String token = request.getHeader(jwtUtils.getHeader());
//        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZWVyIiwiY3JlYXRlZCI6MTc1MjIyODYzMDI2MywiZXhwIjoxNzUyMzE1MDMwfQ._CJgm7bn7VdVYMum9oOptx5kQX1IrNq_yC1gQXzsGw07hYFsm4WzHJndLvC3XBAmM86d5JazAm5SK-1YQGx0Gg";

;        if (token != null) {
            // 验证令牌
            if (jwtUtils.validateToken(token)) {
                // 从令牌获取用户名
                String username = jwtUtils.getUsernameFromToken(token);
                
                // 检查Redis中是否存在该令牌
                LoginUser loginUser = redisUtils.get(LOGIN_USER + username);

                if (ObjectUtils.isEmpty(loginUser)){
                    throw new AuthExceptions(AuthExceptions.LOGIN_AUTHENTICATION_EXPIRED,"登录认证已过期");
                }else {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    loginUser,
                                    null,
                                    loginUser.getAuthorities());
                    
                    // 设置认证信息到Security上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        
        // 次过滤器放行，继续过滤器链
        filterChain.doFilter(request, response);
    }
}