package com.tefo.customerservice.core.service;


import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.exception.AccessDeniedToCustomerActionException;
import com.tefo.library.commonutils.constants.ExceptionMessages;
import com.tefo.library.commonutils.validation.AccessFieldValidation;
import com.tefo.library.commonutils.validation.MultipleAccessFieldValidation;
import com.tefo.library.commonutils.validation.utils.AccessFieldValidationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccessProcessingServiceImpl implements AccessProcessingService {

    private final UserService userService;

    @Override
    public void processAccessFieldPermission(Object obj) {
        if (Objects.isNull(obj)) {
            return;
        }
        List<String> userPermissions = userService.getUserPermission().stream().map(UserPermissionBasicDto::getCode).toList();
        Class<?> dtoClass = obj.getClass();

        try {
            for (Field field : dtoClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(MultipleAccessFieldValidation.class)) {
                    MultipleAccessFieldValidation annotation = field.getAnnotation(MultipleAccessFieldValidation.class);
                    for (AccessFieldValidation validation : annotation.values()) {
                        AccessFieldValidationType annotationType = validation.validationType();
                        String[] permissions = validation.permissions();
                        field.setAccessible(true);

                        switch (annotationType) {
                            case SET_NULL -> {
                                if (Collections.disjoint(userPermissions, Arrays.asList(permissions))) {
                                    field.set(obj, null);
                                }
                            }
                            case THROW_EXC -> {
                                if (Objects.nonNull(field.get(obj))) {
                                    if (Collections.disjoint(userPermissions, Arrays.asList(permissions))) {
                                        throw new AccessDeniedToCustomerActionException(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION);
                                    }
                                }
                            }
                            default ->
                                    throw new IllegalArgumentException("Unsupported validation type: " + annotationType);
                        }
                    }
                }
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
}
