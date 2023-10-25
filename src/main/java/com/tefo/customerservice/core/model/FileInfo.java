package com.tefo.customerservice.core.model;

import lombok.Data;

import java.util.Date;

@Data
public class FileInfo {
    private String id;
    private String name;
    private String accessUrl;
    private String createdBy;
    private String updatedBy;
    private Date createdAt;
}
