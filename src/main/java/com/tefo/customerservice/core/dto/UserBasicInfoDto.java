package com.tefo.customerservice.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoDto {
    private String id;
    private String userName;
    private Set<UserPermissionBasicDto> permissions;
}
