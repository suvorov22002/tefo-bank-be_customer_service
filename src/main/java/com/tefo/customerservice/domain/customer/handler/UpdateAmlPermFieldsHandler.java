package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;

import java.util.List;

public class UpdateAmlPermFieldsHandler implements UpdateCustomerHandler {
    private UpdateCustomerHandler nextHandler;

    @Override
    public void setNextHandler(UpdateCustomerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes) {
        if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_AML.name())) {
            savedCustomer.setAml(customer.getAml());
            if (savedCustomer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID) && customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
                savedCustomer.getNaturalCustomerInfo().setPep(customer.getNaturalCustomerInfo().isPep());
            }
        }
        if (nextHandler != null) {
            nextHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
        }
    }
}
