package com.tefo.customerservice.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerSettingsDTO {
    private Integer internalCodeLength;
    private Boolean isUsedUnitCodeInInternalCode;
    private Integer internalCodeAllowedSymbolsDictionaryValueId;
    private Integer internalCodeFreeSegmentDictionaryValueId;
    private Integer minAgeForOnboarding;
    @JsonProperty("KYCPeriod")
    private Integer KYCPeriod;
    private Integer documentTypeForPhotoDictionaryValueId;
    private Integer documentTypeForSignatureDictionaryValueId;
    private Integer defaultLegalFormForNaturalPersonDictionaryValueId;
    private Integer defaultEconomicSectorForNaturalPersonDictionaryValueId;
}
