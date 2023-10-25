package com.tefo.customerservice.core.config;

import com.tefo.customerservice.core.interceptor.StatusPermissionValidationHandlerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final StatusPermissionValidationHandlerInterceptor statusPermissionValidationHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statusPermissionValidationHandlerInterceptor);
    }
}
