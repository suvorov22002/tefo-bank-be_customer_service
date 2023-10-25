package com.tefo.customerservice.domain.customer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParentsInfo {

    private String fathersFirstName;
    private String fathersMiddleName;
    private String fathersLastName;
    private String mothersFirstName;
    private String mothersMiddleName;
    private String mothersLastName;
}
