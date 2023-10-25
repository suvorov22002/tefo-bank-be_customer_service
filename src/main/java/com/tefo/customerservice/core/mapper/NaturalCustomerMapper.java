package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.NaturalCustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.NaturalCustomerResponseDto;
import com.tefo.customerservice.domain.customer.model.NaturalCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {BirthDetailsMapper.class, BasicMapper.class}, componentModel = "spring")
public abstract class NaturalCustomerMapper {

    @Mapping(target = "birthDetails", source = "dto.birthDetails")
    public abstract NaturalCustomer toEntity(NaturalCustomerRequestDto dto);

    @Mapping(target = "salutationName", source = "salutationId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "genderName", source = "genderId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "languageName", source = "languageId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "educationName", source = "educationId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "maritalStatusName", source = "maritalStatusId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "userName", source = "userId", qualifiedByName = "getUserName")
    public abstract NaturalCustomerResponseDto toDto(NaturalCustomer entity);
}
