package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.domain.customer.dto.CustomerListResponseDto;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.CustomerResponseDto;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {
        KycMapper.class,
        ClassificationMapper.class,
        AMLMapper.class,
        LegalCustomerMapper.class,
        NaturalCustomerMapper.class,
        BasicMapper.class},
        componentModel = "spring"
)
public abstract class CustomerMapper {

    @Mapping(target = "countryId", source = "dto", qualifiedByName = "getCountryId")
    @Mapping(target = "unitId", source = "dto", qualifiedByName = "getUnitId")
    @Mapping(target = "statusId", source = "dto", qualifiedByName = "getDefaultCustomerStatusId")
    @Mapping(target = "status", source = "dto", qualifiedByName = "getDefaultCustomerStatus")
    @Mapping(target = "naturalCustomerInfo", source = "dto.naturalCustomerInfo")
    public abstract CustomerEntity toEntity(CustomerRequestDto dto);

    @Mapping(target = "status", source = "entity.statusId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "type", source = "entity.typeId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "countryName", source = "countryId", qualifiedByName = "getCountryName")
    @Mapping(target = "unitName", source = "entity", qualifiedByName = "getUnitName")
    @Mapping(target = "rejectReasonName", source = "rejectReasonId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "rejectedBy", source = "rejectedBy", qualifiedByName = "getUserName")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "getUserName")
    public abstract CustomerResponseDto toDto(CustomerEntity entity);

    @Mapping(target = "unitName", source = "entity", qualifiedByName = "getUnitName")
    @Mapping(target = "status", source = "entity.statusId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "type", source = "entity.typeId", qualifiedByName = "getDictionaryValue")
    @Mapping(target = "residency", source = "entity", qualifiedByName = "getResidency")
    @Mapping(target = "relationshipManagerName", source = "entity.kyc.relationshipManagerId", qualifiedByName = "getUserName")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "getUserName")
    public abstract CustomerListResponseDto toListCustomerResponseDto(CustomerEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "aml", ignore = true)
    @Mapping(target = "rejectReasonId", ignore = true)
    @Mapping(target = "rejectComment", ignore = true)
    @Mapping(target = "rejectedBy", ignore = true)
    @Mapping(target = "rejectedAt", ignore = true)
    @Mapping(target = "naturalCustomerInfo.pep", ignore = true)
    @Mapping(target = "classification.segmentId", ignore = true)
    @Mapping(target = "classification.portfolioId", ignore = true)
    @Mapping(target = "classification.costCenterId", ignore = true)
    @Mapping(target = "classification.profitCenterId", ignore = true)
    @Mapping(target = "classification.riskClassId", ignore = true)
    @Mapping(target = "classification.riskClassAdjustedId", ignore = true)
    @Mapping(target = "kyc.monitoringModeId", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract CustomerEntity update(CustomerEntity source, @MappingTarget CustomerEntity target);

    public List<CustomerListResponseDto> toDtoList(List<CustomerEntity> entityList) {
        return entityList.stream()
                .map(this::toListCustomerResponseDto)
                .toList();
    }

    public List<BasicInfoDto<String>> toBasicInfoList(List<CustomerEntity> entityList) {
        List<BasicInfoDto<String>> basicInfoDtos = new ArrayList<>();
        if (entityList == null) {
            return basicInfoDtos;
        }

        entityList.forEach(customerEntity ->
                basicInfoDtos.add(new BasicInfoDto<>(customerEntity.getId(),
                        customerEntity.getCode() + " " + customerEntity.getShortName()))
        );
        return basicInfoDtos;
    }
}
