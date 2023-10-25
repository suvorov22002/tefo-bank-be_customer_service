package com.tefo.customerservice.domain.customer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Getter
@Setter
@FieldNameConstants
public class LegalCustomer {
    private String legalName;
    private LocalDate enregistrationDate;
    private String registrationNumber;
    private Integer countryOfRiskId;
    private Boolean financialInstitution;
    private Boolean bank;
    private String bic;
}
