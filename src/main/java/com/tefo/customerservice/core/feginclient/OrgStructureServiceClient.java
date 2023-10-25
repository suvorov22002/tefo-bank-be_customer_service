package com.tefo.customerservice.core.feginclient;

import com.tefo.customerservice.core.dto.UnitActiveResponse;
import com.tefo.customerservice.core.dto.UnitResponseDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "orgStructureService", url = "http://" + "${org-structure.service.host}" + RestEndpoints.ORG_SERVICE_BASE, configuration = {FeignClientErrorDecoder.class})
public interface OrgStructureServiceClient {

    @GetMapping(value = "units/{unitId}")
    UnitResponseDto getUnitById(@PathVariable("unitId") String unitId);

    @GetMapping(value = "units/{unitId}/active/non-virtual")
    UnitActiveResponse isUnitActiveAndNonVirtual(@PathVariable String unitId);
}
