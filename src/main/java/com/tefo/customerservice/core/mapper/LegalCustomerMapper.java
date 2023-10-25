package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.LegalCustomerResponseDto;
import com.tefo.customerservice.domain.customer.model.LegalCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {BasicMapper.class})
public abstract class LegalCustomerMapper {

    @Mapping(target = "countryOfRiskName", source = "countryOfRiskId", qualifiedByName = "getCountryName")
    public abstract LegalCustomerResponseDto toDto(LegalCustomer entity);
}
