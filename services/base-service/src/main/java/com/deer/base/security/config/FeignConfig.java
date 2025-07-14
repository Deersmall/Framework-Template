package com.deer.base.security.config;

import com.deer.base.security.filter.FeignTokenInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Autowired
    private FeignTokenInterceptor tokenInterceptor;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return tokenInterceptor;
    }
    
    // 其他配置...
}