package com.tefo.customerservice.domain.customer.model;

import com.tefo.library.commonutils.validation.AccessFieldValidation;
import com.tefo.library.commonutils.validation.MultipleAccessFieldValidation;
import com.tefo.library.commonutils.validation.utils.AccessFieldValidationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Classification {
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private Integer riskClassId;
    private Integer legalFormId;
    private Integer economicSectorId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private Integer riskClassAdjustedId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private Integer segmentId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private Integer portfolioId;
    // TODO: implement after TBD will be ready
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private String costCenterId;
    // TODO: implement after TBD will be ready
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.THROW_EXC),
    })
    private String profitCenterId;
}
