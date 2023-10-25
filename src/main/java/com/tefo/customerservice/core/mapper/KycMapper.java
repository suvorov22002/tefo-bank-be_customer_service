package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.KYCResponseDto;
import com.tefo.customerservice.domain.customer.model.KYC;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BasicMapper.class})
public abstract class KycMapper {

    @Mapping(target = "relationshipManagerName", source = "relationshipManagerId", qualifiedByName = "getUserName")
    @Mapping(target = "introducerName", source = "introducerId", qualifiedByName = "getCustomerName")
    @Mapping(target = "kycStatusName", source = "kycStatusId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "monitoringModeName", source = "monitoringModeId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "nextReviewDate", source = "entity", qualifiedByName = "calculateNextReviewDate")
    public abstract KYCResponseDto toDto(KYC entity);
}
