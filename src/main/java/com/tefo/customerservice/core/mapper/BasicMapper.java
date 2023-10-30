package com.tefo.customerservice.core.mapper;

import com.tefo.customerservice.core.dto.*;
import com.tefo.customerservice.core.feginclient.CoreSettingsServiceClient;
import com.tefo.customerservice.core.feginclient.DictionaryServiceClient;
import com.tefo.customerservice.core.feginclient.IdentityServiceClient;
import com.tefo.customerservice.core.feginclient.OrgStructureServiceClient;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerStatus;
import com.tefo.customerservice.domain.customer.repository.CustomerRepository;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class BasicMapper {

    @Autowired
    CoreSettingsServiceClient coreSettingsServiceClient;

    @Autowired
    DictionaryServiceClient dictionaryServiceClient;

    @Autowired
    OrgStructureServiceClient orgStructureServiceClient;

    @Autowired
    IdentityServiceClient identityServiceClient;

    @Autowired
    CustomerRepository customerRepository;


    @Named("getResidency")
    public boolean getResidency(CustomerEntity entity) {
        BankProfileDTO bankProfile = coreSettingsServiceClient.getBankProfile();
        CountryResponseDto country = dictionaryServiceClient.getCountryById(entity.getCountryId());
        return country.getName().equalsIgnoreCase(bankProfile.getCountry());
    }

    @Named("getCountryId")
    public Integer getCountryId(CustomerRequestDto dto) {
        CountryResponseDto country = dictionaryServiceClient.getCountryById(dto.getCountryId());
        return Objects.nonNull(country) ? country.getId() : null;
    }

    @Named("getUnitId")
    public String getUnitId(CustomerRequestDto dto) {
        UnitResponseDto unit = orgStructureServiceClient.getUnitById(dto.getUnitId());
        return Objects.nonNull(unit) ? unit.getId() : null;
    }

    @Named("getCountryName")
    public String getCountryName(Integer countryId) {
        if (Objects.nonNull(countryId)) {
            CountryResponseDto country = dictionaryServiceClient.getCountryById(countryId);
            return Objects.nonNull(country) ? country.getName() : null;
        }
        return null;
    }

    @Named("getUnitName")
    public String getUnitName(CustomerEntity entity) {
        UnitResponseDto unit = orgStructureServiceClient.getUnitById(entity.getUnitId());
        return Objects.nonNull(unit) ? unit.getName() : null;
    }

    @Named("getDictionaryValue")
    public String getDictionaryValue(Integer dictionaryId) {
        if (Objects.nonNull(dictionaryId)) {
            BasicInfoDto<Integer> dictionaryValue = dictionaryServiceClient.getDictionaryValueBasicInfo(dictionaryId);
            return Objects.nonNull(dictionaryValue) ? dictionaryValue.getName() : null;
        }
        return null;
    }

    @Named("getUserName")
    public String getUserName(String userId) {
        if (StringUtils.isNotEmpty(userId)) {
            UserResponseDto userResponseDto = identityServiceClient.getUserById(userId);
            return Objects.nonNull(userResponseDto) ? userResponseDto.getUserName() : null;
        }
        return null;
    }

    @Named("getCustomerName")
    public String getCustomerName(String customerId) {
        try {
            Optional<CustomerEntity> customer = customerRepository.findById(customerId);
            return customer.map(CustomerEntity::getShortName).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Named("getDefaultCustomerStatusId")
    public Integer getDefaultCustomerStatusId(CustomerRequestDto dto) {
        List<DictionaryValueResponseDto> values = dictionaryServiceClient.getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE);
        Optional<DictionaryValueResponseDto> optionalDictionaryValue = values.stream().filter(value -> value.getName().equalsIgnoreCase(CustomerStatus.PROSPECT.getValue())).findFirst();
        return optionalDictionaryValue.map(DictionaryValueResponseDto::getId).orElse(null);
    }

    @Named("getDefaultCustomerStatus")
    public String getDefaultCustomerStatus(CustomerRequestDto dto) {
        List<DictionaryValueResponseDto> values = dictionaryServiceClient.getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE);
        Optional<DictionaryValueResponseDto> optionalDictionaryValue = values.stream().filter(value -> value.getName().equalsIgnoreCase(CustomerStatus.PROSPECT.getValue())).findFirst();
        return optionalDictionaryValue.map(DictionaryValueResponseDto::getName).orElse(null);
    }
}
