package com.tefo.customerservice.core.model;

import com.tefo.customerservice.core.enumeration.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    private String id;

    private StorageAttributes storageAttributes;

    private String name;

    private int typeIdx;

    private List<File> files = new ArrayList<>();

    private FileStatus status = FileStatus.ACTIVE;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private Set<String> tags = new HashSet<>();
}
