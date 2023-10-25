package com.tefo.customerservice.core.dto;

import com.tefo.library.commonutils.validation.MandatoryField;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BankProfileDTO {
    private String shortName;
    private String longName;
    private String streetLine;
    private String city;
    private String region;
    private String zipCode;
    @MandatoryField
    private String country;
    private String shortPhoneNumber;
    private String phoneCode;
    private String email;
    private String codeGroup;
    private String swiftCode;
    Set<FieldValueDTO> customFieldsValues = new HashSet<>();
}
