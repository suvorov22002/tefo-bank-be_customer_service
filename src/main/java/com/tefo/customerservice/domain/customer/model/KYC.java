package com.tefo.customerservice.domain.customer.model;

import com.tefo.library.commonutils.validation.AccessFieldValidation;
import com.tefo.library.commonutils.validation.MultipleAccessFieldValidation;
import com.tefo.library.commonutils.validation.utils.AccessFieldValidationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class KYC {
    private String relationshipManagerId;
    private LocalDate firstContactDate;
    private String introducerId;
    private boolean sponsor;
    private Integer kycStatusId;
    private LocalDate previousReviewDate;
    private LocalDate nextReviewDate;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.THROW_EXC)
    })
    private Integer monitoringModeId;
    private boolean banksRelatedParty;
}
