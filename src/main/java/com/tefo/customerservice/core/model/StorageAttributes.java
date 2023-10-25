package com.tefo.customerservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageAttributes {
    String entityName;
    String branchId;
    String clientId;
}
