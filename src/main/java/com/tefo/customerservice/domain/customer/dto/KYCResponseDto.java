package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.domain.customer.model.KYC;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KYCResponseDto extends KYC {
    private String relationshipManagerName;
    private String introducerName;
    private String kycStatusName;
    private String monitoringModeName;
}
