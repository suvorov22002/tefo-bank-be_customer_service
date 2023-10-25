package com.tefo.customerservice.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tefo.customerservice.core.enumeration.CountryStatus;
import com.tefo.library.customdata.TemplatedDTO;
import com.tefo.library.customdata.field.value.FieldValueDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CountryResponseDto implements TemplatedDTO {
    private Integer id;
    @JsonProperty("ISOAlpha3Code")
    private String ISOAlpha3Code;
    @JsonProperty("ISOAlpha2Code")
    private String ISOAlpha2Code;
    @JsonProperty("ISONumericCode")
    private String ISONumericCode;
    private String shortName;
    private String name;
    private String phoneCode;
    private Integer phoneLengthMin;
    private Integer phoneLengthMax;
    private CountryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private Set<FieldValueDTO> customFieldsValues;
}
