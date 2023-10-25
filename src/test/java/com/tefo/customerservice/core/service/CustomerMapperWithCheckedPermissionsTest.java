package com.tefo.customerservice.core.service;

import com.tefo.customerservice.core.dto.UserBasicInfoDto;
import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.exception.AccessDeniedToCustomerActionException;
import com.tefo.customerservice.core.mapper.CustomerMapper;
import com.tefo.customerservice.domain.customer.dto.CustomerListResponseDto;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.CustomerResponseDto;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.library.commonutils.auth.RequestScope;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.ExceptionMessages;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;
import com.tefo.library.commonutils.constants.ValidationMessages;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class CustomerMapperWithCheckedPermissionsTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private AccessProcessingService accessProcessingService;
    @Mock
    private UserService userService;
    @Mock
    private RequestScope requestScope;
    @InjectMocks
    private CustomerMapperWithCheckedPermissions mapperWithCheckedPermissions;

    private CustomerEntity customer;
    private CustomerListResponseDto customerListResponseDto;
    private CustomerResponseDto customerResponseDto;
    private CustomerRequestDto customerRequestDto;
    private Set<UserPermissionBasicDto> userPermissionBasicDtos;
    private UserBasicInfoDto userBasicInfoDto;

    @BeforeEach
    void setUp() {
        customer = Instancio.create(CustomerEntity.class);
        customerListResponseDto = Instancio.create(CustomerListResponseDto.class);
        customerResponseDto = Instancio.create(CustomerResponseDto.class);
        customerRequestDto = Instancio.create(CustomerRequestDto.class);

        UserPermissionBasicDto amlUserPermissionBasicDto = new UserPermissionBasicDto();
        amlUserPermissionBasicDto.setId("1");
        amlUserPermissionBasicDto.setCode("EDIT_CUSTOMER_AML");

        userPermissionBasicDtos = new HashSet<>();
        userPermissionBasicDtos.add(amlUserPermissionBasicDto);
        userBasicInfoDto = new UserBasicInfoDto();
        userBasicInfoDto.setPermissions(userPermissionBasicDtos);
    }

    @Test
    @DisplayName("should return customer entity and trigger injected service, mapper")
    void shouldReturnEntity() {
        when(userService.getUserPermission()).thenReturn(userPermissionBasicDtos);
        when(customerMapper.toEntity(customerRequestDto)).thenReturn(customer);
        doNothing().when(accessProcessingService).processAccessFieldPermission(any());

        CustomerEntity result = mapperWithCheckedPermissions.createDtoToEntity(customerRequestDto);

        assertEquals(customer, result);

        verify(userService).getUserPermission();
        verify(customerMapper).toEntity(any(CustomerRequestDto.class));
        verify(accessProcessingService, times(3)).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should throw exception when user does not have aml permission and blacklist is true")
    void shouldThrowExceptionWhenBlacklistTrue() {
        customerRequestDto.getAml().setBlacklist(true);

        when(userService.getUserPermission()).thenReturn(Set.of());

        AccessDeniedToCustomerActionException exception = assertThrows(AccessDeniedToCustomerActionException.class, () -> {
            mapperWithCheckedPermissions.createDtoToEntity(customerRequestDto);
        });

        assertEquals(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION, exception.getMessage());

        verify(userService).getUserPermission();
        verify(customerMapper, times(0)).toEntity(any(CustomerRequestDto.class));
        verify(accessProcessingService, times(0)).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should throw exception when user does not have aml permission and pep is true")
    void shouldThrowExceptionWhenPepTrue() {
        customerRequestDto.setTypeId(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID);
        customerRequestDto.getAml().setBlacklist(false);
        customerRequestDto.getNaturalCustomerInfo().setPep(true);

        when(userService.getUserPermission()).thenReturn(Set.of());

        AccessDeniedToCustomerActionException exception = assertThrows(AccessDeniedToCustomerActionException.class, () -> {
            mapperWithCheckedPermissions.createDtoToEntity(customerRequestDto);
        });

        assertEquals(ExceptionMessages.ACCESS_DENY_TO_CUSTOMER_ACTION, exception.getMessage());

        verify(userService).getUserPermission();
        verify(customerMapper, times(0)).toEntity(any(CustomerRequestDto.class));
        verify(accessProcessingService, times(0)).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should return customer entity without annotation processing and trigger injected service, mapper")
    void shouldReturnEntityWithoutAnnProcessing() {
        when(userService.getUserPermission()).thenReturn(userPermissionBasicDtos);
        when(customerMapper.toEntity(customerRequestDto)).thenReturn(customer);

        CustomerEntity result = mapperWithCheckedPermissions.updateDtoToEntity(customerRequestDto);

        assertEquals(customer, result);

        verify(userService).getUserPermission();
        verify(customerMapper).toEntity(any(CustomerRequestDto.class));
    }

    @Test
    @DisplayName("should return customer entity with default relationship manager and trigger injected service, mapper")
    void shouldReturnEntityWithRelationshipManager() {
        UserPermissionBasicDto rmUserPermissionBasicDto = new UserPermissionBasicDto();
        rmUserPermissionBasicDto.setId("1");
        rmUserPermissionBasicDto.setCode("EDIT_CUSTOMER_RM");
        userPermissionBasicDtos.add(rmUserPermissionBasicDto);
        customerRequestDto.getKyc().setRelationshipManagerId(null);

        when(userService.getUserPermission()).thenReturn(userPermissionBasicDtos);
        when(customerMapper.toEntity(customerRequestDto)).thenReturn(customer);

        CustomerEntity result = mapperWithCheckedPermissions.updateDtoToEntity(customerRequestDto);

        assertEquals(customer, result);

        verify(userService).getUserPermission();
        verify(customerMapper).toEntity(any(CustomerRequestDto.class));
    }

    @Test
    @DisplayName("should throw exception when kyc object is null and pep is true")
    void shouldThrowExceptionWhenKysIsNull() {
        customerRequestDto.setKyc(null);

        when(userService.getUserPermission()).thenReturn(Set.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mapperWithCheckedPermissions.updateDtoToEntity(customerRequestDto);
        });

        assertEquals(ValidationMessages.VALUE_SHOULD_NOT_BE_NULL, exception.getMessage());

        verify(userService).getUserPermission();
        verify(customerMapper, times(0)).toEntity(any(CustomerRequestDto.class));
        verify(accessProcessingService, times(0)).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should throw exception when user does not have edit rm perm and pep is true")
    void shouldThrowExceptionWhenRmPermNotExist() {
        customerRequestDto.getKyc().setRelationshipManagerId(null);

        when(userService.getUserPermission()).thenReturn(Set.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            mapperWithCheckedPermissions.updateDtoToEntity(customerRequestDto);
        });

        assertEquals(ValidationMessages.VALUE_SHOULD_NOT_BE_NULL, exception.getMessage());

        verify(userService).getUserPermission();
        verify(customerMapper, times(0)).toEntity(any(CustomerRequestDto.class));
        verify(accessProcessingService, times(0)).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should return customer dto and trigger injected service, mapper")
    void shouldReturnDto() {
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);
        doNothing().when(accessProcessingService).processAccessFieldPermission(customerResponseDto.getClassification());

        CustomerResponseDto result = mapperWithCheckedPermissions.toDto(customer);

        assertEquals(customerResponseDto, result);

        verify(customerMapper).toDto(any(CustomerEntity.class));
        verify(accessProcessingService).processAccessFieldPermission(any());
    }

    @Test
    @DisplayName("should return customer basic info list and trigger injected mapper")
    void shouldReturnBasicInfoList() {
        BasicInfoDto<String> basicInfoDto = new BasicInfoDto<>();
        basicInfoDto.setId("1");
        basicInfoDto.setName("name");

        when(customerMapper.toBasicInfoList(List.of(customer))).thenReturn(List.of(basicInfoDto));

        List<BasicInfoDto<String>> result = mapperWithCheckedPermissions.toBasicInfoList(List.of(customer));

        assertEquals(List.of(basicInfoDto), result);

        verify(customerMapper).toBasicInfoList(anyList());
    }

    @Test
    @DisplayName("should return customer dto list and trigger injected mapper")
    void shouldReturnDtoList() {
        when(customerMapper.toListCustomerResponseDto(customer)).thenReturn(customerListResponseDto);

        List<CustomerListResponseDto> result = mapperWithCheckedPermissions.toDtoList(List.of(customer));

        assertEquals(List.of(customerListResponseDto), result);

        verify(customerMapper).toListCustomerResponseDto(any(CustomerEntity.class));
    }
}