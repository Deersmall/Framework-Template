package com.deer.framework.config;

import com.deer.framework.filter.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF和CORS
                .csrf().disable()
//                .cors().disable()

                // 基于JWT，不需要Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // 授权配置
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/auth/login")
                .anonymous()
                // 其他所有请求都需要认证
                .anyRequest().authenticated().and()

                // 添加JWT过滤器，在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 异常处理
                .exceptionHandling()
                // 认证失败处理
                .authenticationEntryPoint(authenticationEntryPoint())
                // 权限不足处理
                .accessDeniedHandler(accessDeniedHandler());

        return http.build();
    }

    /**
     * 为登录密码加密与校验
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    /**
     * 直接暴露 AuthenticationManager 作为 Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 认证失败处理
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpServletResponse.SC_UNAUTHORIZED);
            result.put("message", "认证失败: " + authException.getMessage());
            result.put("data", null);

            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        };
    }

    /**
     * 权限不足处理
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpServletResponse.SC_FORBIDDEN);
            result.put("message", "权限不足: " + accessDeniedException.getMessage());
            result.put("data", null);

            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        };
    }
}