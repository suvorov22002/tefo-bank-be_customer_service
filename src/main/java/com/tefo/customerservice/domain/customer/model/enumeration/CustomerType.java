package com.tefo.customerservice.domain.customer.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CustomerType {
    NATURAL("Natural person"),
    LEGAL("Legal entity");

    private String value;
}
