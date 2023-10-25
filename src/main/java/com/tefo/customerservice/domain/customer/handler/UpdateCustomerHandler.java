package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.domain.customer.model.CustomerEntity;

import java.util.List;

public interface UpdateCustomerHandler {
    void setNextHandler(UpdateCustomerHandler nextHandler);
    void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes);
}
