package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.core.mapper.CustomerMapper;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;

import java.util.List;

public class UpdateRmBoPermFieldsHandler implements UpdateCustomerHandler {
    private UpdateCustomerHandler nextHandler;
    private final CustomerMapper customerMapper;

    public UpdateRmBoPermFieldsHandler(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Override
    public void setNextHandler(UpdateCustomerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes) {
        if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_BO.name()) || userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name())) {
            customerMapper.update(customer, savedCustomer);
        }
        if (nextHandler != null) {
            nextHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
        }
    }
}
