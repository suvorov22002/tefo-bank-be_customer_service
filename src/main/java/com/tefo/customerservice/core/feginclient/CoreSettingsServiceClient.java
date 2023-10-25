package com.tefo.customerservice.core.feginclient;

import com.tefo.customerservice.core.dto.BankProfileDTO;
import com.tefo.customerservice.core.dto.CustomerSettingsDTO;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "coreSettingsService", url = "http://" + "${core-settings.service.host}" + RestEndpoints.CORE_SETTINGS_BASE, configuration = {FeignClientErrorDecoder.class})
public interface CoreSettingsServiceClient {

    @GetMapping(value = "bank-profile")
    BankProfileDTO getBankProfile();

    @GetMapping(value = "bank-settings/customers")
    CustomerSettingsDTO getPartOfSettings();

}
