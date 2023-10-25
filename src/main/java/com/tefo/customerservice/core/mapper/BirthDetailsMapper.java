package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.BirthDetailsRequestDto;
import com.tefo.customerservice.domain.customer.dto.BirthDetailsResponseDto;
import com.tefo.customerservice.domain.customer.model.BirthDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BasicMapper.class})
public abstract class BirthDetailsMapper {

    public abstract BirthDetails toEntity(BirthDetailsRequestDto dto);

    @Mapping(target = "birthDatePrecisionName", source = "birthDatePrecisionId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "countryOfBirthName", source = "countryOfBirthId", qualifiedByName = "getCountryName")
    public abstract BirthDetailsResponseDto toDto(BirthDetails entity);
}
