package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.ParentsInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
public class NaturalCustomerRequestDto {

    private String firstName;
    private String middleName;
    private String lastName;
    private String maidenName;
    private Integer salutationId;
    private Integer genderId;
    private Integer languageId;
    private Integer educationId;
    private String photo;
    private String signatureSample;
    private Integer maritalStatusId;
    private Integer numberOfDependentPersons;
    private boolean bankEmployee;
    private String userId;
    private boolean pep;
    private BirthDetailsRequestDto birthDetails;
    private ParentsInfo parentsInfo;
}
