package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.AMLResponseDto;
import com.tefo.customerservice.domain.customer.model.AML;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BasicMapper.class})
public abstract class AMLMapper {

    @Mapping(target = "amlRiskRatingName", source = "amlRiskRatingId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "amlRiskRatingAdjustedName", source = "amlRiskRatingAdjustedId", qualifiedByName = "getDictionaryValue")
    public abstract AMLResponseDto toDto(AML entity);
}
