package com.tefo.customerservice.core.service;

import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.dto.UserResponseDto;
import com.tefo.customerservice.core.feginclient.IdentityServiceClient;
import com.tefo.customerservice.core.feginclient.OrgStructureServiceClient;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerStatus;
import com.tefo.customerservice.domain.customer.repository.CustomerRepository;
import com.tefo.library.commonutils.auth.RequestScope;
import com.tefo.library.commonutils.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ObjectProvider<IdentityServiceClient> identityServiceClient;
    private final RequestScope requestScope;
    private final CustomerRepository customerRepository;
    private final ObjectProvider<OrgStructureServiceClient> orgStructureServiceClient;

    @Override
    public Set<UserPermissionBasicDto> getUserPermission() {
        String userId = requestScope.getCurrentUserId();
        return identityServiceClient.getIfUnique().getUserBasicPermissionsInfo(userId).getPermissions();
    }

    @Override
    public boolean isAllowCustomerAction(String customerId, String actionType) {
        Set<UserPermissionBasicDto> userPermission = getUserPermission();
        Set<String> userPermissionCodes = userPermission.stream().map(UserPermissionBasicDto::getCode).collect(Collectors.toSet());

        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new EntityNotFoundException("Customer not found");
        }
        CustomerEntity customer = optionalCustomer.get();

        switch (actionType) {
            case "REJECT" -> {
                return isAllowRejectAction(customer, userPermissionCodes);
            }
            case "VALIDATE" -> {
                return isAllowValidateAction(customer, userPermissionCodes);
            }
            case "AUTHORIZE" -> {
                return isAllowAuthorizeAction(customer, userPermissionCodes);
            }
            case "EDIT" -> {
                return isAllowEditAction(customer, userPermissionCodes);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public UserResponseDto getUserById() {
        String currentUserId = requestScope.getCurrentUserId();
        return identityServiceClient.getIfUnique().getUserById(currentUserId);
    }

    @Override
    public String getUserUnit() {
        UserResponseDto userResponseDto = getUserById();
        String unitId = userResponseDto.getDefaultUnitId();
        if (StringUtils.isNotEmpty(unitId)) {
            return orgStructureServiceClient.getIfUnique().isUnitActiveAndNonVirtual(unitId).isActive() ? unitId : null;
        }
        return null;
    }

    private boolean isAllowRejectAction(CustomerEntity customer, Set<String> userPermissionCodes) {
        boolean userHasAuthorizePermission = userPermissionCodes.contains(CustomerPermissionCode.AUTHORISE_CUSTOMER.name());
        boolean isPenAuthStatus = customer.getStatus().equals(CustomerStatus.PENDING_AUTH.getValue());

        boolean userHasEditBoPermission = userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_BO.name());
        boolean isProspectStatus = customer.getStatus().equals(CustomerStatus.PROSPECT.getValue());

        boolean userHasEditRmPermission = userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name());
        boolean isRelManager = requestScope.getCurrentUserId().equals(customer.getKyc().getRelationshipManagerId());

        return (userHasAuthorizePermission && isPenAuthStatus)
                || (userHasEditBoPermission && isProspectStatus)
                || (userHasEditRmPermission && isProspectStatus && isRelManager);
    }

    private boolean isAllowValidateAction(CustomerEntity customer, Set<String> userPermissionCodes) {
        boolean userHasEditBoPermission = userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_BO.name());
        boolean isProspectStatus = customer.getStatus().equals(CustomerStatus.PROSPECT.getValue());

        boolean serHasEditRmPermission = userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name());
        boolean isRelManager = requestScope.getCurrentUserId().equals(customer.getKyc().getRelationshipManagerId());

        return (userHasEditBoPermission && isProspectStatus) || (serHasEditRmPermission && isProspectStatus && isRelManager);
    }

    private boolean isAllowAuthorizeAction(CustomerEntity customer, Set<String> userPermissionCodes) {
        boolean userHasAuthPermission = userPermissionCodes.contains(CustomerPermissionCode.AUTHORISE_CUSTOMER.name());
        boolean isPenAuthStatus = customer.getStatus().equals(CustomerStatus.PENDING_AUTH.getValue());

        return userHasAuthPermission && isPenAuthStatus;
    }

    private boolean isAllowEditAction(CustomerEntity customer, Set<String> userPermissionCodes) {
        boolean userHasEditBoPermission = !Collections.disjoint(userPermissionCodes,
                List.of(CustomerPermissionCode.EDIT_CUSTOMER_BO.name(),
                        CustomerPermissionCode.EDIT_CUSTOMER_RISK.name(),
                        CustomerPermissionCode.EDIT_CUSTOMER_BUSINESS.name(),
                        CustomerPermissionCode.EDIT_CUSTOMER_AML.name()));

        boolean userHasEditRmPermission = userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name());
        boolean isRelManager = requestScope.getCurrentUserId().equals(customer.getKyc().getRelationshipManagerId());

        return (userHasEditBoPermission) || (userHasEditRmPermission && isRelManager);
    }
}
