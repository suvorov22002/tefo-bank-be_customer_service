package com.tefo.customerservice.domain.customer.model;

import com.tefo.library.commonutils.validation.AccessFieldValidation;
import com.tefo.library.commonutils.validation.MultipleAccessFieldValidation;
import com.tefo.library.commonutils.validation.utils.AccessFieldValidationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AML {
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_AML"}, validationType = AccessFieldValidationType.THROW_EXC)
    })
    private Integer amlRiskRatingId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_AML"}, validationType = AccessFieldValidationType.THROW_EXC)
    })
    private Integer amlRiskRatingAdjustedId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_AML"}, validationType = AccessFieldValidationType.THROW_EXC)
    })
    private LocalDateTime lastCheckDate;
    private boolean blacklist;
}
