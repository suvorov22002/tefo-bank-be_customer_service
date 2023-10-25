package com.tefo.customerservice.domain.customer.dto;

import com.tefo.customerservice.core.utils.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerListResponseDto extends BaseEntity {

    private String id;
    private String code;
    private String type;
    private String shortName;
    private boolean residency;
    private String unitName;
    private String relationshipManagerName;
    private String status;
}
