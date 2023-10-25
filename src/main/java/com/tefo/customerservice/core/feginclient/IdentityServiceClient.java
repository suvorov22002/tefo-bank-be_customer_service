package com.tefo.customerservice.core.feginclient;

import com.tefo.customerservice.core.dto.UserBasicInfoDto;
import com.tefo.customerservice.core.dto.UserResponseDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identityService", url = "http://" + "${identity.service.host}" + RestEndpoints.IDENTITY_SERVICE_BASE, configuration = {FeignClientErrorDecoder.class})
public interface IdentityServiceClient {

    @GetMapping("users/{id}")
    UserResponseDto getUserById(@PathVariable String id);

    @GetMapping("users/{userId}/basic-info")
    UserBasicInfoDto getUserBasicPermissionsInfo(@PathVariable String userId);
}
