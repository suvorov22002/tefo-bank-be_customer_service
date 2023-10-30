package com.tefo.customerservice.core.service;

import com.tefo.customerservice.RequestScopeImpl;
import com.tefo.customerservice.core.dto.UserBasicInfoDto;
import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.feginclient.IdentityServiceClient;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.KYC;
import com.tefo.customerservice.domain.customer.repository.CustomerRepository;
import com.tefo.library.commonutils.exception.EntityNotFoundException;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class UserServiceImplTest {

    @Mock
    private ObjectProvider<IdentityServiceClient> objectProvider;
    @Mock
    private IdentityServiceClient identityServiceClient;
    @Mock
    private RequestScopeImpl requestScope;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private static String userId = "userId";
    private String customerId;
    private Set<UserPermissionBasicDto> userPermissionBasicDtos;
    private UserBasicInfoDto userBasicInfoDto;
    private UserPermissionBasicDto userPermissionBasicDto;

    @BeforeEach
    void setUp() {
        userPermissionBasicDto = new UserPermissionBasicDto();
        userPermissionBasicDto.setId("1");
        userPermissionBasicDto.setCode("code");
        userPermissionBasicDtos = Set.of(userPermissionBasicDto);
        userBasicInfoDto = new UserBasicInfoDto();
        userBasicInfoDto.setPermissions(userPermissionBasicDtos);

        customerId = "customerId";

        when(objectProvider.getIfUnique()).thenReturn(identityServiceClient);
    }

    private static Stream<Arguments> getActionType() {
        UserPermissionBasicDto authorizeCustomerUserPermissionBasicDto;
        UserPermissionBasicDto editBOUserPermissionBasicDto;
        UserPermissionBasicDto editRmUserPermissionBasicDto;

        authorizeCustomerUserPermissionBasicDto = new UserPermissionBasicDto();
        authorizeCustomerUserPermissionBasicDto.setId("1");
        authorizeCustomerUserPermissionBasicDto.setCode("AUTHORISE_CUSTOMER");

        editBOUserPermissionBasicDto = new UserPermissionBasicDto();
        editBOUserPermissionBasicDto.setId("2");
        editBOUserPermissionBasicDto.setCode("EDIT_CUSTOMER_BO");

        editRmUserPermissionBasicDto = new UserPermissionBasicDto();
        editRmUserPermissionBasicDto.setId("3");
        editRmUserPermissionBasicDto.setCode("EDIT_CUSTOMER_RM");

        CustomerEntity customerWithProspectStatus;
        CustomerEntity customerWithPendAuthStatus;
        KYC kyc = new KYC();
        kyc.setRelationshipManagerId(userId);

        customerWithProspectStatus = Instancio.of(CustomerEntity.class)
                .set(field(CustomerEntity::getStatus), "Prospect")
                .set(field(CustomerEntity::getKyc), kyc)
                .create();

        customerWithPendAuthStatus = Instancio.of(CustomerEntity.class)
                .set(field(CustomerEntity::getStatus), "Pending Authorization")
                .create();


        return Stream.of(
                Arguments.of("REJECT", Set.of(authorizeCustomerUserPermissionBasicDto), customerWithPendAuthStatus, true),
                Arguments.of("REJECT", Set.of(editBOUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("REJECT", Set.of(editRmUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("VALIDATE", Set.of(editBOUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("VALIDATE", Set.of(editRmUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("AUTHORISE", Set.of(authorizeCustomerUserPermissionBasicDto), customerWithPendAuthStatus, true),
                Arguments.of("EDIT", Set.of(editBOUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("EDIT", Set.of(editRmUserPermissionBasicDto), customerWithProspectStatus, true),
                Arguments.of("UPDATE", Set.of(editRmUserPermissionBasicDto), customerWithProspectStatus, false)
        );
    }

    @Test
    @DisplayName("should return user permissions basic info and trigger injected services, repository")
    void shouldReturnUserPermission() {
        when(identityServiceClient.getUserBasicPermissionsInfo(userId)).thenReturn(userBasicInfoDto);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        Set<UserPermissionBasicDto> userPermissionResult = userService.getUserPermission();

        assertEquals(userPermissionBasicDtos, userPermissionResult);
        verify(requestScope).getCurrentUserId();
        verify(objectProvider).getIfUnique();
        verify(identityServiceClient).getUserBasicPermissionsInfo(anyString());
    }

    @Test
    @DisplayName("should throw exception when customer does not exist")
    void shouldThrowExceptionWhenVerifyIfAllowCustomerAction() {
        String expectedMessage = "Customer not found";

        when(identityServiceClient.getUserBasicPermissionsInfo(userId)).thenReturn(userBasicInfoDto);
        when(requestScope.getCurrentUserId()).thenReturn(userId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.isAllowCustomerAction(customerId, "REJECT");
        });
        assertEquals(expectedMessage, exception.getMessage());

        verify(requestScope).getCurrentUserId();
        verify(objectProvider).getIfUnique();
        verify(identityServiceClient).getUserBasicPermissionsInfo(anyString());
        verify(customerRepository).findById(anyString());
    }

    @ParameterizedTest
    @MethodSource("getActionType")
    @DisplayName("should return boolean value if customer action is allowed")
    void shouldReturnIfAllowCustomerAction(String actionType, Set<UserPermissionBasicDto> userPermissionBasicDtos, CustomerEntity customer, boolean returnValue) {
        UserBasicInfoDto userBasicInfoDto = new UserBasicInfoDto();
        userBasicInfoDto.setPermissions(userPermissionBasicDtos);
        when(identityServiceClient.getUserBasicPermissionsInfo(userId)).thenReturn(userBasicInfoDto);
        when(requestScope.getCurrentUserId()).thenReturn(userId);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        boolean allowCustomerAction = userService.isAllowCustomerAction(customerId, actionType);

        assertEquals(returnValue, allowCustomerAction);

        verify(requestScope, atLeast(1)).getCurrentUserId();
        verify(objectProvider, atLeast(1)).getIfUnique();
        verify(identityServiceClient, atLeast(1)).getUserBasicPermissionsInfo(anyString());
        verify(customerRepository, atLeast(1)).findById(anyString());
    }
}