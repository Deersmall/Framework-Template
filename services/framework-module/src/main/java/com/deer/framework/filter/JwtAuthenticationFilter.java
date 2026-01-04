package com.deer.framework.filter;


import com.deer.entities.system.vo.LoginUser;
import com.deer.framework.exception.AuthExceptions;
import com.deer.framework.utils.JwtUtils;
import com.deer.framework.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.HashMap;
import java.util.Map;

import static com.deer.framework.constants.RedisConstants.LOGIN_USER;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final ObjectMapper objectMapper;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, RedisUtils redisUtils, ObjectMapper objectMapper) {
        this.jwtUtils = jwtUtils;
        this.redisUtils = redisUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 从请求头获取令牌
        String token = request.getHeader(jwtUtils.getHeader());
//        token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkZWVyIiwiY3JlYXRlZCI6MTc1MjIyODYzMDI2MywiZXhwIjoxNzUyMzE1MDMwfQ._CJgm7bn7VdVYMum9oOptx5kQX1IrNq_yC1gQXzsGw07hYFsm4WzHJndLvC3XBAmM86d5JazAm5SK-1YQGx0Gg";

        try {
            if (token != null) {
                // 验证令牌
                if (jwtUtils.validateToken(token)) {
                    // 从令牌获取用户名
                    String username = jwtUtils.getUsernameFromToken(token);

                    // 检查Redis中是否存在该令牌
                    LoginUser loginUser = redisUtils.get(LOGIN_USER + username);

                    if (ObjectUtils.isEmpty(loginUser)){
                        // 用户信息不存在，返回401未认证
                        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "登录认证已过期");
                        return;
                    }else {
//                        验证通过，续签redis时长
                        redisUtils.expire(LOGIN_USER + username, 60 * 30);

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        loginUser,
                                        null,
                                        loginUser.getAuthorities());

                        // 设置认证信息到Security上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }else {
                    // Token验证失败，返回401未认证
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Token验证失败");
                    return;
                }
            }
        }catch (AuthExceptions ex){
            // 捕获认证异常，返回401未认证
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
            return;
        }catch (Exception ex) {
            // 其他异常，返回500服务器错误
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
            return;
        }

        
        // 次过滤器放行，继续过滤器链
        filterChain.doFilter(request, response);
    }



    /**
     * 发送错误响应
     * @param response HttpServletResponse对象
     * @param status HTTP状态码
     * @param message 错误信息
     */
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new HashMap<>();
        result.put("code", status);
        result.put("message", message);
        result.put("data", null);

        response.getWriter().write(objectMapper.writeValueAsString(result));
    }


}