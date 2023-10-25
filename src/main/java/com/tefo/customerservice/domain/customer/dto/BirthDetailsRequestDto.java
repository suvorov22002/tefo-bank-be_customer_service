package com.tefo.customerservice.domain.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BirthDetailsRequestDto {
    private LocalDate birthDate;
    private Integer birthDatePrecisionId;
    private Integer countryOfBirthId;
    private String regionOfBirth;
    private String placeOfBirth;
    private LocalDate deceasedDate;
}
