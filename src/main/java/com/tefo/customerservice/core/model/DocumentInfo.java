package com.tefo.customerservice.core.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class DocumentInfo {
    private String id;
    private String name;
    private List<FileInfo> files = new ArrayList<>();
    private Date createdAt;
    private String createdBy;
    private Date updatedAt;
    private String updatedBy;
    private Set<String> tags;
    private String entityName;
    private String branchId;
    private String clientId;
}
