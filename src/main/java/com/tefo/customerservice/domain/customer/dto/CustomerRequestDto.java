package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.AML;
import com.tefo.customerservice.domain.customer.model.Classification;
import com.tefo.customerservice.domain.customer.model.KYC;
import com.tefo.customerservice.domain.customer.model.LegalCustomer;
import com.tefo.library.commonutils.validation.ConditionalMandatoryFields;
import com.tefo.library.commonutils.validation.MandatoryField;
import com.tefo.library.commonutils.validation.MultipleConditionalMandatoryFields;
import com.tefo.library.customdata.TemplatedDTO;
import com.tefo.library.customdata.field.validation.ValidationErrorMessages;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldNameConstants
@MultipleConditionalMandatoryFields({
        @ConditionalMandatoryFields(
                fieldNames = {"typeId"},
                expectedValues = {"87"},
                conditionalFields = {"naturalCustomerInfo", "naturalCustomerInfo.lastName"}
        ),
        @ConditionalMandatoryFields(
                fieldNames = {"typeId"},
                expectedValues = {"88"},
                conditionalFields = {"legalCustomerInfo", "legalCustomerInfo.legalName", "legalCustomerInfo.financialInstitution", "legalCustomerInfo.bank"}
        ),
        @ConditionalMandatoryFields(
                fieldNames = {"typeId", "legalCustomerInfo.bank"},
                expectedValues = {"88", "true"},
                conditionalFields = {"legalCustomerInfo.bic"},
                message = ValidationErrorMessages.MANDATORY_ERROR_MESSAGE
        )
})
public class CustomerRequestDto implements TemplatedDTO {
    @MandatoryField
    private Integer typeId;
    private String shortName;
    private String nameToReturn;
    @MandatoryField
    private Integer countryId;
    @MandatoryField
    private String unitId;
    private Classification classification;
    private KYC kyc;
    private AML aml;
    private LocalDate bankruptcyDate;
    private LocalDate relationshipEndDate;
    private String notes;
    private LegalCustomer legalCustomerInfo;
    private NaturalCustomerRequestDto naturalCustomerInfo;
    private Set<FieldValueDTO> customFieldsValues = new HashSet<>();
}
