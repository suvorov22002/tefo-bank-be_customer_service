package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.core.utils.BaseEntity;
import com.tefo.library.commonutils.validation.MandatoryField;
import com.tefo.library.customdata.TemplatedDTO;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto extends BaseEntity implements TemplatedDTO {

    private String id;
    private String masterId;
    @MandatoryField
    private Integer typeId;
    @MandatoryField
    private String type;
    private String code;
    @MandatoryField
    private Integer statusId;
    @MandatoryField
    private String status;
    @MandatoryField
    private String shortName;
    private String nameToReturn;
    @MandatoryField
    private int countryId;
    @MandatoryField
    private String countryName;
    @MandatoryField
    private String unitId;
    @MandatoryField
    private String unitName;
    private ClassificationResponseDto classification;
    private KYCResponseDto kyc;
    private AMLResponseDto aml;
    private LocalDate bankruptcyDate;
    private LocalDate relationshipEndDate;
    private String notes;
    private String rejectedBy;
    private LocalDateTime rejectedAt;
    private String rejectReasonName;
    private String rejectComment;
    private LegalCustomerResponseDto legalCustomerInfo;
    private NaturalCustomerResponseDto naturalCustomerInfo;
    private Set<FieldValueDTO> customFieldsValues = new HashSet<>();
}
