package com.tefo.customerservice.domain.customer.model;

import com.tefo.library.commonutils.validation.MandatoryField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
public class NaturalCustomer {
    private String firstName;
    private String middleName;
    @MandatoryField
    private String lastName;
    private String maidenName;
    private Integer salutationId;
    private Integer genderId;
    private Integer languageId;
    private Integer educationId;
    private CustomerDocument photoDocument;
    private CustomerDocument signatureSampleDocument;
    private Integer maritalStatusId;
    private Integer numberOfDependentPersons;
    private boolean bankEmployee;
    private String userId;
    private boolean pep;
    private BirthDetails birthDetails;
    private ParentsInfo parentsInfo;
}
