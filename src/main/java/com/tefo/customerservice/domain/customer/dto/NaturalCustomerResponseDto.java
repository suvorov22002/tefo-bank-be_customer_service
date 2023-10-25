package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.CustomerDocument;
import com.tefo.customerservice.domain.customer.model.ParentsInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaturalCustomerResponseDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String maidenName;
    private Integer salutationId;
    private String salutationName;
    private Integer genderId;
    private String genderName;
    private Integer languageId;
    private String languageName;
    private Integer educationId;
    private String educationName;
    private CustomerDocument photoDocument;
    private CustomerDocument signatureSampleDocument;
    private Integer maritalStatusId;
    private String maritalStatusName;
    private Integer numberOfDependentPersons;
    private boolean bankEmployee;
    private String userId;
    private String userName;
    private boolean pep;
    private BirthDetailsResponseDto birthDetails;
    private ParentsInfo parentsInfo;
}
