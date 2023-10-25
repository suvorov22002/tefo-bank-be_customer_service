package com.tefo.customerservice.domain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectCustomerDto {

    private Integer rejectReasonId;
    private String rejectComment;
}
