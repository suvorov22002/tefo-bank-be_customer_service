package com.tefo.customerservice.domain.customer.model;

import com.tefo.customerservice.domain.customer.model.enumeration.CustomerDocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDocument {

    private String documentId;
    private CustomerDocumentType type;
    private String fileId;
    private String fileUrl;
    private String fileName;
}
