package com.tefo.customerservice.core.dto;

import com.tefo.customerservice.core.enumeration.UserStatus;
import com.tefo.library.customdata.TemplatedDTO;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto implements TemplatedDTO {
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String phoneCountryCode;
    private String phoneNumber;
    private Integer languageDictionaryValueId;
    private String languageDictionaryValueName;
    private String defaultUnitId;
    private String defaultUnitName;
    private boolean allowMultipleSessions;
    private boolean allowDefaultPassword;
    private UserStatus status;
    private LocalDateTime lastLoginDate;
    private Set<FieldValueDTO> customFieldsValues = new HashSet<>();
}
