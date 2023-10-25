package com.tefo.customerservice.core.service;

import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.exception.AccessDeniedToCustomerActionException;
import com.tefo.customerservice.core.mapper.CustomerMapper;
import com.tefo.customerservice.domain.customer.dto.CustomerListResponseDto;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.CustomerResponseDto;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;
import com.tefo.library.commonutils.auth.RequestScope;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.ExceptionMessages;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;
import com.tefo.library.commonutils.constants.ValidationMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerMapperWithCheckedPermissions {

    private final CustomerMapper customerMapper;
    private final AccessProcessingService accessProcessingService;
    private final UserService userService;
    private final RequestScope requestScope;

    public CustomerEntity createDtoToEntity(CustomerRequestDto dto) {
        Set<UserPermissionBasicDto> userPermission = userService.getUserPermission();
        List<String> userPermissionCodes = userPermission.stream().map(UserPermissionBasicDto::getCode).toList();
        checkRelationshipManagerField(dto, userPermissionCodes);
        if (Objects.nonNull(dto.getAml())) {
            if ((dto.getAml().isBlacklist()) && !userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_AML.name())) {
                throw new AccessDeniedToCustomerActionException(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION);
            }
        }

        if (Objects.nonNull(dto.getNaturalCustomerInfo()) && dto.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            if (dto.getNaturalCustomerInfo().isPep() && !userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_AML.name())) {
                throw new AccessDeniedToCustomerActionException(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION);
            }
        }
        accessProcessingService.processAccessFieldPermission(dto.getClassification());
        accessProcessingService.processAccessFieldPermission(dto.getKyc());
        accessProcessingService.processAccessFieldPermission(dto.getAml());
        return customerMapper.toEntity(dto);
    }

    public CustomerEntity updateDtoToEntity(CustomerRequestDto dto) {
        List<String> userPermissionCodes = userService.getUserPermission().stream()
                .map(UserPermissionBasicDto::getCode).toList();
        checkRelationshipManagerField(dto, userPermissionCodes);
        return customerMapper.toEntity(dto);
    }


    public CustomerResponseDto toDto(CustomerEntity customer) {
        CustomerResponseDto customerResponseDto = customerMapper.toDto(customer);
        accessProcessingService.processAccessFieldPermission(customerResponseDto.getClassification());
        return customerResponseDto;
    }

    public List<BasicInfoDto<String>> toBasicInfoList(List<CustomerEntity> customers) {
        return customerMapper.toBasicInfoList(customers);
    }

    public List<CustomerListResponseDto> toDtoList(List<CustomerEntity> customers) {
        return customers.stream()
                .map(customerMapper::toListCustomerResponseDto)
                .toList();
    }

    private void checkRelationshipManagerField(CustomerRequestDto dto, List<String> userPermissionCodes) {
        if (Objects.nonNull(dto.getKyc())) {
            if (Objects.isNull(dto.getKyc().getRelationshipManagerId())) {
                if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name())) {
                    dto.getKyc().setRelationshipManagerId(requestScope.getCurrentUserId());
                } else {
                    throw new IllegalArgumentException(ValidationMessages.VALUE_SHOULD_NOT_BE_NULL);
                }
            }
        } else {
            throw new IllegalArgumentException(ValidationMessages.VALUE_SHOULD_NOT_BE_NULL);
        }
    }
}
