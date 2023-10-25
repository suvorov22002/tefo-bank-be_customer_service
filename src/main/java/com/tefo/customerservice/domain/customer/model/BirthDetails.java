package com.tefo.customerservice.domain.customer.model;

import com.tefo.customerservice.domain.customer.dto.BirthDetailsRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BirthDetails extends BirthDetailsRequestDto {
    private int age;
}
