package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.core.dto.CustomerSettingsDTO;
import com.tefo.customerservice.core.feginclient.CoreSettingsServiceClient;
import com.tefo.customerservice.core.mapper.CustomerMapper;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;

import java.util.List;
import java.util.Objects;

public class UpdateRmBoPermFieldsHandler implements UpdateCustomerHandler {
    private UpdateCustomerHandler nextHandler;
    private final CustomerMapper customerMapper;
    private final CoreSettingsServiceClient coreSettingsServiceClient;

    public UpdateRmBoPermFieldsHandler(CustomerMapper customerMapper, CoreSettingsServiceClient coreSettingsServiceClient) {
        this.customerMapper = customerMapper;
        this.coreSettingsServiceClient = coreSettingsServiceClient;
    }

    @Override
    public void setNextHandler(UpdateCustomerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes) {
        if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_BO.name()) || userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name())) {
            setNextReviewDate(customer, savedCustomer);
            customerMapper.update(customer, savedCustomer);
        }
        if (nextHandler != null) {
            nextHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
        }
    }

    private void setNextReviewDate(CustomerEntity customer, CustomerEntity savedCustomer) {
        CustomerSettingsDTO partOfSettings = coreSettingsServiceClient.getPartOfSettings();
        if (Objects.nonNull(partOfSettings) && Objects.nonNull(partOfSettings.getKYCPeriod())) {
            if (Objects.nonNull(customer.getKyc().getPreviousReviewDate()) &&
                    (Objects.isNull(customer.getKyc().getNextReviewDate())
                            || customer.getKyc().getNextReviewDate().isAfter(customer.getKyc().getPreviousReviewDate().plusYears(partOfSettings.getKYCPeriod())))
                    || customer.getKyc().getNextReviewDate().isEqual(savedCustomer.getKyc().getNextReviewDate())
            ) {
                customer.getKyc().setNextReviewDate(customer.getKyc().getPreviousReviewDate().plusYears(partOfSettings.getKYCPeriod()));
            }
        }
        customer.getKyc().setNextReviewDate(customer.getKyc().getNextReviewDate());
    }

}
