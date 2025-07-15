package com.deer.framework.filter;

import com.deer.framework.utils.SecurityUtils;
import com.deer.entities.system.vo.LoginUser;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class FeignTokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 从安全上下文中获取当前认证信息
        LoginUser loginUser = SecurityUtils.getLoginUser();

        if (!ObjectUtils.isEmpty(loginUser)) {
            template.header("Authorization", loginUser.getToken());
        }
    }

}