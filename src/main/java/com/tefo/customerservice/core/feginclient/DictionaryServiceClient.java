package com.tefo.customerservice.core.feginclient;

import com.tefo.customerservice.core.dto.CountryResponseDto;
import com.tefo.customerservice.core.dto.DictionaryValueResponseDto;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "dictionaryService", url = "http://" + "${dictionary.service.host}", configuration = {FeignClientErrorDecoder.class})
public interface DictionaryServiceClient {

    @GetMapping(value = RestEndpoints.COUNTRY_URL + "/{id}")
    CountryResponseDto getCountryById(@PathVariable Integer id);

    @GetMapping(value = RestEndpoints.DICTIONARY_URL + "/values/{id}/basic-info")
    BasicInfoDto<Integer> getDictionaryValueBasicInfo(@PathVariable Integer id);

    @GetMapping(RestEndpoints.DICTIONARY_URL + "/values/all")
    List<DictionaryValueResponseDto> getDictionaryValuesByDictionaryCode(@RequestParam String dictionaryCode);
}
