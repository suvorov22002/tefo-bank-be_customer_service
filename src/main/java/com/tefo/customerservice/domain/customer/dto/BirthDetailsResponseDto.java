package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.BirthDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BirthDetailsResponseDto extends BirthDetails {
    private String birthDatePrecisionName;
    private String countryOfBirthName;
}
