package com.tefo.customerservice.domain.customer.model.enumeration;

import lombok.Getter;

@Getter
public enum CustomerStatus {

    PROSPECT("Prospect"),
    REJECTED("Rejected"),
    PENDING_AUTH("Pending Authorization"),
    INACTIVE("Inactive"),
    ACTIVE("Active"),
    CLOSED("Closed");

    private final String value;

    CustomerStatus(String value) {
        this.value = value;
    }
}
