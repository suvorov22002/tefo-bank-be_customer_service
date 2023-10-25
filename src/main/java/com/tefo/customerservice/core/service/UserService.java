package com.tefo.customerservice.core.service;

import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.dto.UserResponseDto;

import java.util.Set;

public interface UserService {
    Set<UserPermissionBasicDto> getUserPermission();

    boolean isAllowCustomerAction(String customerId, String actionType);

    UserResponseDto getUserById();

    String getUserUnit();
}
