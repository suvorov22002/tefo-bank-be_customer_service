package com.tefo.customerservice.core.model;

import lombok.Data;

@Data
public class DocumentRequest {
    private String entityName;
    private String branchId;
    private String clientId;
}
