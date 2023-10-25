package com.tefo.customerservice.core.interceptor;

import com.tefo.customerservice.core.annotation.PreValidateStatusAndPermission;
import com.tefo.customerservice.core.exception.AccessDeniedToCustomerActionException;
import com.tefo.customerservice.core.service.UserService;
import com.tefo.library.commonutils.constants.ExceptionMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class StatusPermissionValidationHandlerInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public StatusPermissionValidationHandlerInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!((HandlerMethod) handler).hasMethodAnnotation(PreValidateStatusAndPermission.class)) {
            return true;
        }

        String type = ((HandlerMethod) handler).getMethodAnnotation(PreValidateStatusAndPermission.class).type();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String customerId = (String) pathVariables.get("customerId");

        if (!userService.isAllowCustomerAction(customerId, type)) {
            throw new AccessDeniedToCustomerActionException(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION);
        }
        return true;
    }
}
