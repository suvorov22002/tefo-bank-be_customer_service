package com.tefo.customerservice.domain.customer.handler;

import com.tefo.customerservice.domain.customer.model.Classification;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.KYC;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;

import java.util.List;
import java.util.Objects;

public class UpdateRiskPermFieldsHandler implements UpdateCustomerHandler {
    private UpdateCustomerHandler nextHandler;

    @Override
    public void setNextHandler(UpdateCustomerHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public void handleUpdate(CustomerEntity savedCustomer, CustomerEntity customer, List<String> userPermissionCodes) {
        if (userPermissionCodes.contains(CustomerPermissionCode.EDIT_CUSTOMER_RISK.name())) {
            if (Objects.nonNull(customer.getClassification())) {
                Classification classification = savedCustomer.getClassification();
                if (Objects.isNull(classification)) {
                    classification = new Classification();
                }
                classification.setRiskClassId(customer.getClassification().getRiskClassId());
                classification.setRiskClassAdjustedId(customer.getClassification().getRiskClassAdjustedId());
                savedCustomer.setClassification(classification);
            }
            if (Objects.nonNull(customer.getKyc())) {
                KYC kyc = savedCustomer.getKyc();
                if (Objects.isNull(kyc)) {
                    kyc = new KYC();
                }
                kyc.setMonitoringModeId(customer.getKyc().getMonitoringModeId());
                savedCustomer.setKyc(kyc);
            }
        }
        if (nextHandler != null) {
            nextHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
        }
    }
}
