package com.deer.base.eureka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Eureka 安全配置
 */
@Configuration
public class EurekaSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // 禁用 CSRF 保护
            .authorizeRequests()
            .anyRequest().authenticated()  // 所有请求需要认证
            .and()
            .httpBasic();  // 使用 HTTP Basic 认证
    }
}