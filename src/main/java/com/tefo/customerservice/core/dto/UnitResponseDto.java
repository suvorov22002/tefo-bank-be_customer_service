package com.tefo.customerservice.core.dto;

import com.tefo.customerservice.core.enumeration.UnitStatus;
import com.tefo.library.customdata.TemplatedDTO;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UnitResponseDto implements TemplatedDTO {
    private String id;
    private String unitTypeId;
    private String unitTypeName;
    private String parentId;
    private String parentName;
    private String code;
    private String name;
    private Boolean isDataRestricted;
    private UnitStatus status;
    private String streetLine;
    private String city;
    private String region;
    private String zipCode;
    private String phoneCode;
    private String shortPhoneNumber;
    private String email;
    private Set<FieldValueDTO> customFieldsValues = new HashSet<>();
}
