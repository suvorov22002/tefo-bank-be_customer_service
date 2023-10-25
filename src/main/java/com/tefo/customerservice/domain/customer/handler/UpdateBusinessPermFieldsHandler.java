package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.domain.customer.model.Classification;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;

import java.util.List;
import java.util.Objects;

public class UpdateBusinessPermFieldsHandler implements UpdateCustomerHandler {
    private UpdateCustomerHandler nextHandler;

    @Override
    public void setNextHandler(UpdateCustomerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes) {
        if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_BUSINESS.name())) {
            if (Objects.nonNull(customer.getClassification())) {
                Classification classification = savedCustomer.getClassification();
                if (Objects.isNull(classification)) {
                    classification = new Classification();
                }
                classification.setPortfolioId(customer.getClassification().getPortfolioId());
                classification.setSegmentId(customer.getClassification().getSegmentId());
                classification.setCostCenterId(customer.getClassification().getCostCenterId());
                classification.setProfitCenterId(customer.getClassification().getProfitCenterId());
                savedCustomer.setClassification(classification);
            } else {
                savedCustomer.setClassification(null);
            }
        }
        if (nextHandler != null) {
            nextHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
        }
    }
}
