package com.tefo.customerservice.domain.customer.dto;

import com.tefo.library.commonutils.validation.AccessFieldValidation;
import com.tefo.library.commonutils.validation.MultipleAccessFieldValidation;
import com.tefo.library.commonutils.validation.utils.AccessFieldValidationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClassificationResponseDto {
    private Integer legalFormId;
    private String legalFormName;
    private Integer economicSectorId;
    private String economicSectorName;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_RISK", "EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private Integer riskClassId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_RISK", "EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String riskClassName;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_RISK", "EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private Integer riskClassAdjustedId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_RISK", "EDIT_CUSTOMER_RISK"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String riskClassAdjustedName;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private Integer segmentId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String segmentName;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private Integer portfolioId;
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String portfolioName;
    // TODO: implement after TBD will be ready
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String costCenterId;
    // TODO: implement after TBD will be ready
    @MultipleAccessFieldValidation(values = {
            @AccessFieldValidation(permissions = {"VIEW_CUSTOMER_BUSINESS", "EDIT_CUSTOMER_BUSINESS"}, validationType = AccessFieldValidationType.SET_NULL)
    })
    private String profitCenterId;
}
