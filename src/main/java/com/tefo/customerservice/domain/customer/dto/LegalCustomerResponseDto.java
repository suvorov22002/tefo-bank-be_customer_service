package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.LegalCustomer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LegalCustomerResponseDto extends LegalCustomer {
    private String countryOfRiskName;
}
