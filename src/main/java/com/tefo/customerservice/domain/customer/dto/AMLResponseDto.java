package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.AML;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AMLResponseDto extends AML {
    private String amlRiskRatingName;
    private String amlRiskRatingAdjustedName;
}
