package com.tefo.customerservice.core.model;

import com.tefo.customerservice.core.enumeration.FileStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class File {
    private String id;
    private String name;
    private FileStatus status = FileStatus.ACTIVE;
    private Date createdAt;
    private String createdBy;
    private String updatedBy;
}
