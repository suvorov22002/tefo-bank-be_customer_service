package com.tefo.customerservice.domain.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tefo.customerservice.core.dto.CreateOnlyDocumentRequest;
import com.tefo.customerservice.core.dto.CustomerSettingsDTO;
import com.tefo.customerservice.core.dto.DictionaryValueResponseDto;
import com.tefo.customerservice.core.dto.UnitResponseDto;
import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.enumeration.FreeSegmentGenerationMode;
import com.tefo.customerservice.core.exception.IllegalCustomerActionException;
import com.tefo.customerservice.core.feginclient.CoreSettingsServiceClient;
import com.tefo.customerservice.core.feginclient.DictionaryServiceClient;
import com.tefo.customerservice.core.feginclient.OrgStructureServiceClient;
import com.tefo.customerservice.core.feginclient.documentservice.DocumentServiceClient;
import com.tefo.customerservice.core.mapper.CustomerMapper;
import com.tefo.customerservice.core.model.Document;
import com.tefo.customerservice.core.model.DocumentInfo;
import com.tefo.customerservice.core.model.DocumentResponse;
import com.tefo.customerservice.core.model.FileInfo;
import com.tefo.customerservice.core.service.UserService;
import com.tefo.customerservice.core.utils.UtilMethods;
import com.tefo.customerservice.domain.customer.dto.BirthDetailsRequestDto;
import com.tefo.customerservice.domain.customer.dto.RejectCustomerDto;
import com.tefo.customerservice.domain.customer.handler.*;
import com.tefo.customerservice.domain.customer.model.*;
import com.tefo.customerservice.domain.customer.model.*;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerDocumentType;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerStatus;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerType;
import com.tefo.customerservice.domain.customer.repository.CustomerRepository;
import com.tefo.customerservice.domain.freesegment.model.SequentialFreeSegment;
import com.tefo.customerservice.domain.freesegment.service.FreeSegmentService;
import com.tefo.library.commonutils.auth.RequestScope;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.ExceptionMessages;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;
import com.tefo.library.commonutils.constants.ValidationMessages;
import com.tefo.library.commonutils.exception.EntityNotFoundException;
import com.tefo.library.commonutils.utils.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final String ALPHANUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMERIC = "0123456789";
    private static final String ALPHANUMERIC_MODE = "Alphanumeric";
    private static final String ALPHABETIC_MODE = "Alphabetic only";
    private static final String NUMERIC_MODE = "Numeric only";

    private final CustomerRepository customerRepository;
    private final DictionaryServiceClient dictionaryServiceClient;
    private final CoreSettingsServiceClient coreSettingsServiceClient;
    private final OrgStructureServiceClient orgStructureServiceClient;
    private final DocumentServiceClient documentServiceClient;
    private final CustomerMapper customerMapper;
    private final FreeSegmentService freeSegmentService;
    private final RequestScope requestScope;
    private final UserService userService;

    @Override
    public List<CustomerEntity> getCustomers() {
        return customerRepository.findAllByOrderByShortName();
    }

    @Override
    public List<CustomerEntity> getActiveCustomers() {
        return customerRepository.findAllByStatusOrderByShortName(CustomerStatus.ACTIVE);
    }

    @Override
    public Page<CustomerEntity> getCustomersPaginated(Pageable pageable) {
        return customerRepository.findAllByOrderByShortName(pageable);
    }

    @Override
    public CustomerEntity getCustomerById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public CustomerEntity createCustomer(CustomerEntity customer) {
        if (StringUtils.isEmpty(customer.getShortName())) {
            customer.setShortName(generateShortName(customer));
        }
        setCustomerAge(customer);
        customer.setCode(generateCode(customer));
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(requestScope.getCurrentUserId());
        // TODO: set master id after duplicate criteria will be defined
        customer.setMasterId(null);
        return customerRepository.save(customer);
    }

    @Override
    public CustomerEntity updateCustomer(CustomerEntity customer, String customerId) {
        CustomerEntity savedCustomer = getCustomerById(customerId);

        updateCustomerDependsOnPermission(customer, savedCustomer);

        setCustomerAge(savedCustomer);
        savedCustomer.setUpdatedBy(requestScope.getCurrentUserId());
        savedCustomer.setUpdatedAt(LocalDateTime.now());

        if (!savedCustomer.getStatus().equals(CustomerStatus.PROSPECT.getValue())
                && !savedCustomer.getStatus().equals(CustomerStatus.REJECTED.getValue())) {
            verifyMandatoryFields(savedCustomer, String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_EDIT_ACTION));
        }
        return customerRepository.save(savedCustomer);
    }

    @Override
    public void deleteCustomerById(String id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void deleteAllCustomer() {
        customerRepository.deleteAll();
    }

    @Override
    public CustomerEntity rejectCustomer(String customerId, RejectCustomerDto dto) {
        CustomerEntity customer = getCustomerById(customerId);
        customer.setRejectReasonId(dto.getRejectReasonId());
        customer.setRejectComment(dto.getRejectComment());
        customer.setRejectedBy(requestScope.getCurrentUserId());
        customer.setRejectedAt(LocalDateTime.now());
        setCustomerStatus(customer, CustomerStatus.REJECTED.getValue());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(requestScope.getCurrentUserId());
        return customerRepository.save(customer);
    }

    @Override
    public CustomerEntity validateCustomer(String customerId) {
        CustomerEntity customer = getCustomerById(customerId);
        verifyMandatoryFields(customer, ValidationMessages.CUSTOMER_VALIDATION_FAILED);

        setCustomerStatus(customer, CustomerStatus.PENDING_AUTH.getValue());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(requestScope.getCurrentUserId());
        return customerRepository.save(customer);
    }

    @Override
    public CustomerEntity authorizeCustomer(String customerId) {
        CustomerEntity customer = getCustomerById(customerId);
        setCustomerStatus(customer, CustomerStatus.INACTIVE.getValue());
        customer.setUpdatedAt(LocalDateTime.now());
        customer.setUpdatedBy(requestScope.getCurrentUserId());
        return customerRepository.save(customer);
    }

    @Override
    public CustomerDocument uploadCustomerFile(String customerId, CustomerDocumentType documentType, MultipartFile file) {
        CustomerEntity customer = getCustomerById(customerId);
        if (customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            CustomerDocument customerDocument = null;
            if (documentType == CustomerDocumentType.PHOTO) {
                customerDocument = customer.getNaturalCustomerInfo().getPhotoDocument();
            } else if (documentType == CustomerDocumentType.SIGNATURE) {
                customerDocument = customer.getNaturalCustomerInfo().getSignatureSampleDocument();
            }

            if (Objects.isNull(customerDocument)) {
                customerDocument = createCustomerDocument(customer, documentType);
            }

            DocumentResponse documentResponse = documentServiceClient.uploadSingleFile(customerDocument.getDocumentId(), file, requestScope.getCurrentUserId());
            DocumentInfo documentInfo = new ObjectMapper().convertValue(documentResponse.getData(), DocumentInfo.class);
            Optional<FileInfo> fileInfo = documentInfo.getFiles().stream().min(Comparator.comparing(FileInfo::getCreatedAt));
            if (fileInfo.isPresent()) {
                customerDocument.setFileId(fileInfo.get().getId());
                customerDocument.setFileName(fileInfo.get().getName());
                customerDocument.setFileUrl(fileInfo.get().getAccessUrl());
            }
            customerRepository.save(customer);
            return customerDocument;
        } else {
            throw new IllegalCustomerActionException(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_UPLOAD_FILE_ACTION));
        }
    }

    @Override
    public void deleteCustomerFile(String customerId, CustomerDocumentType documentType) {
        CustomerEntity customer = getCustomerById(customerId);
        if (customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            CustomerDocument customerDocument = null;
            if (documentType == CustomerDocumentType.PHOTO && Objects.nonNull(customer.getNaturalCustomerInfo().getPhotoDocument())) {
                customerDocument = customer.getNaturalCustomerInfo().getPhotoDocument();
            } else if (documentType == CustomerDocumentType.SIGNATURE && Objects.nonNull(customer.getNaturalCustomerInfo().getSignatureSampleDocument())) {
                customerDocument = customer.getNaturalCustomerInfo().getSignatureSampleDocument();
            }

            if (Objects.isNull(customerDocument)) {
                throw new IllegalCustomerActionException(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_DELETE_FILE_ACTION));
            }
            DocumentResponse response = documentServiceClient.removeFile(customerDocument.getDocumentId(), customerDocument.getFileId(), requestScope.getCurrentUserId());
            customerDocument.setFileId(null);
            customerDocument.setFileName(null);
            customerDocument.setFileUrl(null);
            customerRepository.save(customer);
        } else {
            throw new IllegalCustomerActionException(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_DELETE_FILE_ACTION));
        }
    }

    private String generateCode(CustomerEntity entity) {
        String code = StringUtils.EMPTY;
        CustomerSettingsDTO partOfSettings = coreSettingsServiceClient.getPartOfSettings();
        Boolean isUsedUnitCodeInInternalCode = partOfSettings.getIsUsedUnitCodeInInternalCode();
        UnitResponseDto unit = orgStructureServiceClient.getUnitById(entity.getUnitId());
        if (isUsedUnitCodeInInternalCode) {
            code += unit.getCode();
        }
        BasicInfoDto<Integer> allowedSymbolsValue = dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeAllowedSymbolsDictionaryValueId());
        BasicInfoDto<Integer> freeSegmentValue = dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeFreeSegmentDictionaryValueId());
        String freeSegment = generateFreeSegment(partOfSettings, unit, allowedSymbolsValue.getName(), freeSegmentValue.getName());
        return code + freeSegment;
    }

    public String generateFreeSegment(CustomerSettingsDTO partOfSettings, UnitResponseDto unit, String allowedSymbolsValue, String freeSegmentModeValue) {
        int freeSegmentLength = partOfSettings.getIsUsedUnitCodeInInternalCode() ?
                partOfSettings.getInternalCodeLength() - unit.getCode().length() :
                partOfSettings.getInternalCodeLength();

        if (freeSegmentModeValue.equalsIgnoreCase(FreeSegmentGenerationMode.RANDOM.getValue())) {
            return generateRandomFreeSegment(allowedSymbolsValue, freeSegmentLength);
        } else if (freeSegmentModeValue.equalsIgnoreCase(FreeSegmentGenerationMode.SEQUENTIAL.getValue())) {
            return generateSequentialFreeSegment(allowedSymbolsValue, freeSegmentLength);
        }
        return StringUtils.EMPTY;
    }

    private String generateRandomFreeSegment(String allowedSymbolsValue, int freeSegmentLength) {
        switch (allowedSymbolsValue) {
            case NUMERIC_MODE -> {
                return RandomStringUtils.randomNumeric(freeSegmentLength);
            }
            case ALPHABETIC_MODE -> {
                return RandomStringUtils.randomAlphabetic(freeSegmentLength);
            }
            case ALPHANUMERIC_MODE -> {
                return RandomStringUtils.randomAlphanumeric(freeSegmentLength);
            }
            default -> {
                return StringUtils.EMPTY;
            }
        }
    }

    private String generateSequentialFreeSegment(String allowedSymbolsValue, int freeSegmentLength) {
        switch (allowedSymbolsValue) {
            case NUMERIC_MODE -> {
                return getSequentialString(NUMERIC, FreeSegmentGenerationMode.SEQUENTIAL.getValue(), freeSegmentLength, allowedSymbolsValue);
            }
            case ALPHABETIC_MODE -> {
                return getSequentialString(ALPHABETIC, FreeSegmentGenerationMode.SEQUENTIAL.getValue(), freeSegmentLength, allowedSymbolsValue);
            }
            case ALPHANUMERIC_MODE -> {
                return getSequentialString(ALPHANUMERIC, FreeSegmentGenerationMode.SEQUENTIAL.getValue(), freeSegmentLength, allowedSymbolsValue);
            }
            default -> {
                return StringUtils.EMPTY;
            }
        }
    }

    private synchronized String getSequentialString(String allowedSymbols, String strategy, int freeSegmentLength, String allowedSymbolsValue) {
        String lastSavedFreeSegment = freeSegmentService.getLastFreeSegmentGeneratedWith(allowedSymbolsValue, strategy);
        String nextFreeSegment = SequenceGenerator.generateNextSequentialString(lastSavedFreeSegment, allowedSymbols, freeSegmentLength);
        nextFreeSegment = regenerateFreeSegmentIfNotUnique(nextFreeSegment, allowedSymbols, freeSegmentLength);

        SequentialFreeSegment segment = new SequentialFreeSegment();
        segment.setFreeSegment(nextFreeSegment);
        segment.setAllowedSymbols(allowedSymbolsValue);
        segment.setStrategy(strategy);
        segment.setGeneratedAt(LocalDateTime.now());
        SequentialFreeSegment savedFreeSegment = freeSegmentService.save(segment);
        return savedFreeSegment.getFreeSegment();
    }

    private String regenerateFreeSegmentIfNotUnique(String nextFreeSegment, String allowedSymbols, int freeSegmentLength) {
        List<String> all = freeSegmentService.getAll().stream().map(SequentialFreeSegment::getFreeSegment).toList();
        while (all.contains(nextFreeSegment)) {
            nextFreeSegment = SequenceGenerator.generateNextSequentialString(nextFreeSegment, allowedSymbols, freeSegmentLength);
        }
        return nextFreeSegment;
    }

    private String generateShortName(CustomerEntity customer) {
        if (customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)) {
            return customer.getLegalCustomerInfo().getLegalName();
        } else if (customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            return customer.getNaturalCustomerInfo().getFirstName() + " " + customer.getNaturalCustomerInfo().getLastName();
        } else {
            return StringUtils.EMPTY;
        }
    }

    private void setCustomerAge(CustomerEntity customer) {
        if (customer.getTypeId().equals(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            NaturalCustomer naturalCustomerInfo = customer.getNaturalCustomerInfo();
            BirthDetails birthDetails = naturalCustomerInfo.getBirthDetails();
            if (Objects.nonNull(birthDetails) && Objects.nonNull(birthDetails.getBirthDatePrecisionId())) {
                BasicInfoDto<Integer> dictionaryValueBasicInfo = dictionaryServiceClient.getDictionaryValueBasicInfo(birthDetails.getBirthDatePrecisionId());
                int diffYear = LocalDate.now().getYear() - birthDetails.getBirthDate().getYear();
                switch (dictionaryValueBasicInfo.getName()) {
                    case "Day Month and Year" -> {
                        if ((LocalDate.now().getMonthValue() - birthDetails.getBirthDate().getMonthValue() < 0) ||
                                ((LocalDate.now().getMonthValue() - birthDetails.getBirthDate().getMonthValue() >= 0) &&
                                        (LocalDate.now().getDayOfMonth() - birthDetails.getBirthDate().getDayOfMonth() < 0))) {
                            diffYear--;
                        }
                    }
                    case "Month and Year" -> {
                        if (LocalDate.now().getMonthValue() - birthDetails.getBirthDate().getMonthValue() < 0) {
                            diffYear--;
                        }
                    }
                }
                birthDetails.setAge(diffYear);
            }
        }
    }

    private List<DictionaryValueResponseDto> getDictionaryValuesByDictionaryCode(String dictionaryCode) {
        return dictionaryServiceClient.getDictionaryValuesByDictionaryCode(dictionaryCode);
    }

    private void setCustomerStatus(CustomerEntity customer, String status) {
        List<DictionaryValueResponseDto> dictionaryValues = getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE);
        Optional<DictionaryValueResponseDto> rejectStatus = dictionaryValues.stream().filter(value -> value.getName().equalsIgnoreCase(status)).findFirst();
        rejectStatus.ifPresent(dictionaryValueResponseDto -> {
            customer.setStatusId(dictionaryValueResponseDto.getId());
            customer.setStatus(dictionaryValueResponseDto.getName());
        });
    }

    private void verifyMandatoryFields(CustomerEntity customer, String errorMessage) {
        //TODO: add verification on economic sector
        if (!UtilMethods.validateProperties(customer.getClassification(),
                Optional.ofNullable(customer.getClassification()).map(Classification::getLegalFormId).orElse(null),
                Optional.ofNullable(customer.getClassification()).map(Classification::getRiskClassId).orElse(null))) {
            throw new IllegalArgumentException(errorMessage);
        }
        if (Objects.equals(customer.getTypeId(), SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)) {
            if (!UtilMethods.validateProperties(customer.getNaturalCustomerInfo(),
                    customer.getNaturalCustomerInfo().getGenderId(),
                    customer.getNaturalCustomerInfo().getMaritalStatusId(),
                    customer.getNaturalCustomerInfo().getBirthDetails(),
                    customer.getNaturalCustomerInfo().getSignatureSampleDocument(),
                    Optional.ofNullable(customer.getNaturalCustomerInfo().getSignatureSampleDocument()).map(CustomerDocument::getFileId).orElse(null),
                    Optional.ofNullable(customer.getNaturalCustomerInfo().getBirthDetails()).map(BirthDetailsRequestDto::getBirthDate).orElse(null),
                    Optional.ofNullable(customer.getNaturalCustomerInfo().getBirthDetails()).map(BirthDetailsRequestDto::getBirthDatePrecisionId).orElse(null))) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    private CustomerDocument createCustomerDocument(CustomerEntity customer, CustomerDocumentType documentType) {
        DocumentResponse response = documentServiceClient.createOnlyDocument(createRequestObject(customer, documentType));
        Document document = new ObjectMapper().convertValue(response.getData(), Document.class);

        CustomerDocument customerDocument = new CustomerDocument();
        customerDocument.setDocumentId(document.getId());
        customerDocument.setType(documentType);
        switch (documentType.name()) {
            case "PHOTO" -> customer.getNaturalCustomerInfo().setPhotoDocument(customerDocument);
            case "SIGNATURE" -> customer.getNaturalCustomerInfo().setSignatureSampleDocument(customerDocument);
        }
        return customerDocument;
    }

    private CreateOnlyDocumentRequest createRequestObject(CustomerEntity customer, CustomerDocumentType documentType) {
        CreateOnlyDocumentRequest request = new CreateOnlyDocumentRequest();
        request.setName(documentType.name());
        request.setTags(Set.of());
        request.setTypeIdx(documentType == CustomerDocumentType.PHOTO ?
                SystemDictionaryConstants.DOCUMENT_TYPE_PHOTO_DICTIONARY_VALUE_ID :
                SystemDictionaryConstants.DOCUMENT_TYPE_SIGNATURE_DICTIONARY_VALUE_ID);
        request.setCreatedBy(requestScope.getCurrentUserId());
        request.setClientId(customer.getId());
        request.setBranchId(customer.getUnitId());
        request.setEntityName(bucketName);
        return request;
    }

    private void updateCustomerDependsOnPermission(CustomerEntity customer, CustomerEntity savedCustomer) {
        List<String> userPermissionCodes = userService.getUserPermission().stream().map(UserPermissionBasicDto::getCode).toList();

        UpdateCustomerHandler rmBoPermHandler = new UpdateRmBoPermFieldsHandler(customerMapper);
        UpdateCustomerHandler amlPermHandler = new UpdateAmlPermFieldsHandler();
        UpdateCustomerHandler businessPermHandler = new UpdateBusinessPermFieldsHandler();
        UpdateCustomerHandler riskPermHandler = new UpdateRiskPermFieldsHandler();

        rmBoPermHandler.setNextHandler(amlPermHandler);
        amlPermHandler.setNextHandler(businessPermHandler);
        businessPermHandler.setNextHandler(riskPermHandler);
        riskPermHandler.setNextHandler(null);

        rmBoPermHandler.handleUpdate(savedCustomer, customer, userPermissionCodes);
    }
}
