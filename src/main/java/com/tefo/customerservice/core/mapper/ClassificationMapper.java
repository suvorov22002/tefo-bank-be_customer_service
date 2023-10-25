package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.ClassificationResponseDto;
import com.tefo.customerservice.domain.customer.model.Classification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BasicMapper.class})
public abstract class ClassificationMapper {

    @Mapping(target = "legalFormName", source = "legalFormId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "economicSectorName", source = "economicSectorId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "riskClassName", source = "riskClassId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "riskClassAdjustedName", source = "riskClassAdjustedId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "segmentName", source = "segmentId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "portfolioName", source = "portfolioId", qualifiedByName = "getDictionaryValue")
    public abstract ClassificationResponseDto toDto(Classification entity);
}
