package com.deer.framework.config;

import com.deer.framework.filter.FeignTokenInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    private final FeignTokenInterceptor tokenInterceptor;

    public FeignConfig(FeignTokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return tokenInterceptor;
    }
    
    // 其他配置...
}