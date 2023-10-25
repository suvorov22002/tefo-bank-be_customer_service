package com.tefo.customerservice.core.dto;

import com.tefo.customerservice.core.model.DocumentRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class CreateOnlyDocumentRequest extends DocumentRequest {
    private String name;
    private String createdBy;
    private int typeIdx;
    private Set<String> tags = new HashSet<>();
}
