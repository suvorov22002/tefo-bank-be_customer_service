package com.tefo.customerservice.domain.customer.service;

import com.tefo.customerservice.core.dto.CreateOnlyDocumentRequest;
import com.tefo.customerservice.core.dto.CustomerSettingsDTO;
import com.tefo.customerservice.core.dto.DictionaryValueResponseDto;
import com.tefo.customerservice.core.dto.UnitResponseDto;
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
import com.tefo.customerservice.domain.customer.dto.RejectCustomerDto;
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
import com.tefo.library.commonutils.exception.EntityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private DictionaryServiceClient dictionaryServiceClient;
    @Mock
    private CoreSettingsServiceClient coreSettingsServiceClient;
    @Mock
    private OrgStructureServiceClient orgStructureServiceClient;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private FreeSegmentService freeSegmentService;
    @Mock
    private RequestScope requestScope;
    @Mock
    private DocumentServiceClient documentServiceClient;
    @Mock
    private UserService userService;
    @InjectMocks
    private CustomerServiceImpl customerService;

    private DictionaryValueResponseDto dictionaryValue;
    private CustomerEntity customer;
    private CustomerSettingsDTO partOfSettings;
    private SequentialFreeSegment sequentialFreeSegment;
    private UnitResponseDto unit;
    private String customerId;
    private String userId;

    private static Stream<Arguments> getAllowedSymbolsAndRandomMode() {
        return Stream.of(
                Arguments.of(new BasicInfoDto<>(1, "Numeric only"), new BasicInfoDto<>(1, FreeSegmentGenerationMode.RANDOM.getValue())),
                Arguments.of(new BasicInfoDto<>(2, "Alphabetic only"), new BasicInfoDto<>(1, FreeSegmentGenerationMode.RANDOM.getValue())),
                Arguments.of(new BasicInfoDto<>(3, "Alphanumeric"), new BasicInfoDto<>(2, FreeSegmentGenerationMode.RANDOM.getValue()))
        );
    }

    private static Stream<Arguments> getAllowedSymbolsAndSequentialMode() {
        return Stream.of(
                Arguments.of(new BasicInfoDto<>(1, "Numeric only"), new BasicInfoDto<>(1, FreeSegmentGenerationMode.SEQUENTIAL.getValue()), new BasicInfoDto<>(1, "Day Month and Year")),
                Arguments.of(new BasicInfoDto<>(2, "Alphabetic only"), new BasicInfoDto<>(1, FreeSegmentGenerationMode.SEQUENTIAL.getValue()), new BasicInfoDto<>(2, "Month and Year")),
                Arguments.of(new BasicInfoDto<>(3, "Alphanumeric"), new BasicInfoDto<>(2, FreeSegmentGenerationMode.SEQUENTIAL.getValue()), new BasicInfoDto<>(3, "Year"))
        );
    }

    @BeforeEach
    void setUp() {
        customer = Instancio.create(CustomerEntity.class);
        customerId = "customerId";
        userId = "userId";

        dictionaryValue = Instancio.create(DictionaryValueResponseDto.class);

        partOfSettings = Instancio.create(CustomerSettingsDTO.class);
        unit = Instancio.create(UnitResponseDto.class);
        sequentialFreeSegment = Instancio.create(SequentialFreeSegment.class);
        sequentialFreeSegment.setFreeSegment("freeSegment");
    }

    @Test
    @DisplayName("Should return all customers and trigger injected repository")
    void shouldReturnAllCustomers() {
        when(customerRepository.findAllByOrderByShortName()).thenReturn(List.of(customer));

        assertEquals(customerService.getCustomers(), List.of(customer));

        verify(customerRepository).findAllByOrderByShortName();
    }

    @Test
    @DisplayName("Should return all customers paginated and trigger injected repository")
    void shouldReturnCustomersPaginated() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<CustomerEntity> mockPage = new PageImpl<>(List.of(customer), pageable, List.of(customer).size());

        when(customerRepository.findAllByOrderByShortName(pageable)).thenReturn(mockPage);

        Page<CustomerEntity> result = customerService.getCustomersPaginated(pageable);

        assertEquals(mockPage, result);

        verify(customerRepository).findAllByOrderByShortName(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return customer by id and trigger injected repository")
    void shouldReturnCustomerById() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertEquals(customerService.getCustomerById(customerId), customer);

        verify(customerRepository).findById(anyString());
    }

    @Test
    @DisplayName("Should throw an exception when customer not found by id and trigger injected repository")
    void shouldThrowEcxWhenGetCustomerById() {
        String expectedMessage = "Customer not found";

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        assertEquals(expectedMessage, exception.getMessage());

        verify(customerRepository).findById(anyString());
    }

    @ParameterizedTest
    @MethodSource("getAllowedSymbolsAndRandomMode")
    @DisplayName("Should create legal customer with random code and trigger injected repository")
    void shouldCreateCustomerWithRandomCode(BasicInfoDto<Integer> allowedSymbolsValue, BasicInfoDto<Integer> freeSegmentValue) {
        String legalName = "legalName";
        LegalCustomer legalCustomer = new LegalCustomer();
        legalCustomer.setLegalName(legalName);
        customer.setShortName(StringUtils.EMPTY);
        customer.setTypeId(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID);
        customer.setLegalCustomerInfo(legalCustomer);

        partOfSettings.setIsUsedUnitCodeInInternalCode(false);
        partOfSettings.setInternalCodeAllowedSymbolsDictionaryValueId(1);
        partOfSettings.setInternalCodeFreeSegmentDictionaryValueId(2);
        partOfSettings.setInternalCodeLength(4);
        customer.setUnitId("unitId");

        when(customerRepository.save(customer)).thenReturn(customer);
        when(coreSettingsServiceClient.getPartOfSettings()).thenReturn(partOfSettings);
        when(orgStructureServiceClient.getUnitById(customer.getUnitId())).thenReturn(unit);
        when(dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeAllowedSymbolsDictionaryValueId())).thenReturn(allowedSymbolsValue);
        when(dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeFreeSegmentDictionaryValueId())).thenReturn(freeSegmentValue);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        CustomerEntity customerResult = customerService.createCustomer(customer);

        assertEquals(legalName, customerResult.getShortName());
        assertEquals(userId, customerResult.getUpdatedBy());

        verify(customerRepository).save(any(CustomerEntity.class));
        verify(coreSettingsServiceClient).getPartOfSettings();
        verify(orgStructureServiceClient).getUnitById(anyString());
        verify(dictionaryServiceClient, times(2)).getDictionaryValueBasicInfo(anyInt());
        verify(requestScope).getCurrentUserId();
    }

    @ParameterizedTest
    @MethodSource("getAllowedSymbolsAndSequentialMode")
    @DisplayName("Should create natural customer with sequential code and trigger injected repository")
    void shouldCreateCustomerWithSequentialCode(BasicInfoDto<Integer> allowedSymbolsValue, BasicInfoDto<Integer> freeSegmentValue, BasicInfoDto<Integer> birthDatePrecision) {
        String firstName = "firstName";
        String lastName = "lastName";
        int precisionId = 10;
        BirthDetails birthDetails = new BirthDetails();
        birthDetails.setBirthDate(LocalDateTime.now().minusYears(10).plusMonths(1).toLocalDate());
        birthDetails.setBirthDatePrecisionId(precisionId);
        NaturalCustomer naturalCustomer = new NaturalCustomer();
        naturalCustomer.setFirstName(firstName);
        naturalCustomer.setLastName(lastName);
        naturalCustomer.setBirthDetails(birthDetails);
        customer.setShortName(StringUtils.EMPTY);
        customer.setTypeId(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID);
        customer.setNaturalCustomerInfo(naturalCustomer);

        partOfSettings.setIsUsedUnitCodeInInternalCode(false);
        partOfSettings.setInternalCodeAllowedSymbolsDictionaryValueId(1);
        partOfSettings.setInternalCodeFreeSegmentDictionaryValueId(2);
        partOfSettings.setInternalCodeLength(4);
        customer.setUnitId("unitId");

        when(customerRepository.save(customer)).thenReturn(customer);
        when(coreSettingsServiceClient.getPartOfSettings()).thenReturn(partOfSettings);
        when(orgStructureServiceClient.getUnitById(customer.getUnitId())).thenReturn(unit);
        when(dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeAllowedSymbolsDictionaryValueId())).thenReturn(allowedSymbolsValue);
        when(dictionaryServiceClient.getDictionaryValueBasicInfo(partOfSettings.getInternalCodeFreeSegmentDictionaryValueId())).thenReturn(freeSegmentValue);
        when(freeSegmentService.getLastFreeSegmentGeneratedWith(anyString(), anyString())).thenReturn("0000");
        when(freeSegmentService.getAll()).thenReturn(List.of());
        when(freeSegmentService.save(any(SequentialFreeSegment.class))).thenReturn(sequentialFreeSegment);
        when( dictionaryServiceClient.getDictionaryValueBasicInfo(precisionId)).thenReturn(birthDatePrecision);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        CustomerEntity customerResult = customerService.createCustomer(customer);

        assertEquals(firstName + " " + lastName, customerResult.getShortName());
        assertEquals(userId, customerResult.getUpdatedBy());

        verify(customerRepository).save(any(CustomerEntity.class));
        verify(coreSettingsServiceClient).getPartOfSettings();
        verify(orgStructureServiceClient).getUnitById(anyString());
        verify(dictionaryServiceClient, times(3)).getDictionaryValueBasicInfo(anyInt());
        verify(freeSegmentService).getLastFreeSegmentGeneratedWith(anyString(), anyString());
        verify(freeSegmentService).save(any(SequentialFreeSegment.class));
        verify(requestScope).getCurrentUserId();
    }


    @Test
    @DisplayName("Should update customer by id and trigger injected repository")
    void shouldUpdateCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        CustomerEntity customerResult = customerService.updateCustomer(customer, customerId);

        assertEquals(customer, customerResult);

        verify(customerRepository).findById(anyString());
        verify(customerRepository).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should delete customer by id and trigger injected repository")
    void shouldDeleteCustomerById() {
        doNothing().when(customerRepository).deleteById(customerId);

        customerService.deleteCustomerById(customerId);

        verify(customerRepository).deleteById(anyString());
    }

    @Test
    @DisplayName("Should delete all customer and trigger injected repository")
    void shouldDeleteAllCustomer() {
        doNothing().when(customerRepository).deleteAll();

        customerService.deleteAllCustomer();

        verify(customerRepository).deleteAll();
    }

    @Test
    @DisplayName("Should reject customer, set rejected status and trigger injected repository")
    void shouldRejectCustomer() {
        dictionaryValue.setName(CustomerStatus.REJECTED.getValue());
        dictionaryValue.setId(1);
        int rejectedReasonId = 1;
        String rejectedComment = "comment";
        RejectCustomerDto rejectCustomerDto = new RejectCustomerDto();
        rejectCustomerDto.setRejectReasonId(rejectedReasonId);
        rejectCustomerDto.setRejectComment(rejectedComment);
        customer.setStatus(CustomerStatus.PROSPECT.getValue());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dictionaryServiceClient.getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE))
                .thenReturn(List.of(dictionaryValue));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        CustomerEntity expected = customerService.rejectCustomer(customerId, rejectCustomerDto);

        assertEquals(expected, customer);
        assertEquals(CustomerStatus.REJECTED.getValue(), expected.getStatus());
        assertEquals(rejectedComment, expected.getRejectComment());
        assertEquals(rejectedReasonId, expected.getRejectReasonId());
        assertEquals(userId, expected.getUpdatedBy());

        verify(customerRepository).findById(anyString());
        verify(customerRepository).save(any(CustomerEntity.class));
        verify(dictionaryServiceClient).getDictionaryValuesByDictionaryCode(anyString());
        verify(requestScope, times(2)).getCurrentUserId();
    }

    @Test
    @DisplayName("Should validate customer, set pend auth status and trigger injected repository")
    void shouldValidateCustomer() {
        dictionaryValue.setName(CustomerStatus.PENDING_AUTH.getValue());
        dictionaryValue.setId(1);
        customer.setStatus(CustomerStatus.PROSPECT.getValue());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dictionaryServiceClient.getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE))
                .thenReturn(List.of(dictionaryValue));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        CustomerEntity expected = customerService.validateCustomer(customerId);

        assertEquals(expected, customer);
        assertEquals(CustomerStatus.PENDING_AUTH.getValue(), expected.getStatus());
        assertEquals(userId, expected.getUpdatedBy());

        verify(customerRepository).findById(anyString());
        verify(customerRepository).save(any(CustomerEntity.class));
        verify(dictionaryServiceClient).getDictionaryValuesByDictionaryCode(anyString());
        verify(requestScope).getCurrentUserId();
    }

    @Test
    @DisplayName("Should authorize customer, set inactive status and trigger injected repository")
    void shouldAuthorizeCustomer() {
        dictionaryValue.setName(CustomerStatus.INACTIVE.getValue());
        dictionaryValue.setId(1);
        customer.setStatus(CustomerStatus.PENDING_AUTH.getValue());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(dictionaryServiceClient.getDictionaryValuesByDictionaryCode(SystemDictionaryConstants.STATUS_OF_CUSTOMER_DICTIONARY_CODE))
                .thenReturn(List.of(dictionaryValue));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(requestScope.getCurrentUserId()).thenReturn(userId);

        CustomerEntity expected = customerService.authorizeCustomer(customerId);

        assertEquals(expected, customer);
        assertEquals(CustomerStatus.INACTIVE.getValue(), expected.getStatus());
        assertEquals(userId, expected.getUpdatedBy());

        verify(customerRepository).findById(anyString());
        verify(customerRepository).save(any(CustomerEntity.class));
        verify(dictionaryServiceClient).getDictionaryValuesByDictionaryCode(anyString());
        verify(requestScope).getCurrentUserId();
    }

    @Test
    @DisplayName("Should upload customer file and create document and trigger injected repository, document service")
    void shouldUploadCustomerFile() {
        CustomerDocumentType documentType = CustomerDocumentType.PHOTO;
        MultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());
        customer.getNaturalCustomerInfo().setPhotoDocument(null);
        Document document = new Document();
        document.setId("1");
        DocumentInfo documentInfo = new DocumentInfo();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName("name");
        fileInfo.setId("id");
        fileInfo.setAccessUrl("url");
        documentInfo.setFiles(List.of(fileInfo));
        DocumentResponse response = new DocumentResponse();
        DocumentResponse response2 = new DocumentResponse();
        response.setData(document);
        response2.setData(documentInfo);
        customer.setTypeId(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(documentServiceClient.createOnlyDocument(any(CreateOnlyDocumentRequest.class))).thenReturn(response);
        when(requestScope.getCurrentUserId()).thenReturn(userId);
        when(documentServiceClient.uploadSingleFile(document.getId(), multipartFile, userId)).thenReturn(response2);

        CustomerDocument expected = customerService.uploadCustomerFile(customerId, documentType, multipartFile);

        assertEquals(document.getId(), expected.getDocumentId());
        assertEquals(fileInfo.getId(), expected.getFileId());
        assertEquals(fileInfo.getName(), expected.getFileName());
        assertEquals(fileInfo.getAccessUrl(), expected.getFileUrl());

        verify(customerRepository).findById(anyString());
        verify(documentServiceClient).createOnlyDocument(any(CreateOnlyDocumentRequest.class));
        verify(documentServiceClient).uploadSingleFile(anyString(), any(), anyString());
        verify(requestScope, times(2)).getCurrentUserId();
    }

    @Test
    @DisplayName("Should upload customer file to existing document and trigger injected repository, document service")
    void shouldUploadCustomerFileForExistingDocument() {
        CustomerDocumentType documentType = CustomerDocumentType.SIGNATURE;
        MultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());
        Document document = new Document();
        document.setId("1");
        DocumentInfo documentInfo = new DocumentInfo();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName("name");
        fileInfo.setId("id");
        fileInfo.setAccessUrl("url");
        documentInfo.setFiles(List.of(fileInfo));
        DocumentResponse response = new DocumentResponse();
        DocumentResponse response2 = new DocumentResponse();
        response.setData(document);
        response2.setData(documentInfo);
        customer.setTypeId(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(requestScope.getCurrentUserId()).thenReturn(userId);
        when(documentServiceClient.uploadSingleFile(customer.getNaturalCustomerInfo().getSignatureSampleDocument().getDocumentId(), multipartFile, userId))
                .thenReturn(response2);

        CustomerDocument expected = customerService.uploadCustomerFile(customerId, documentType, multipartFile);

        assertEquals(customer.getNaturalCustomerInfo().getSignatureSampleDocument().getDocumentId(), expected.getDocumentId());
        assertEquals(fileInfo.getId(), expected.getFileId());
        assertEquals(fileInfo.getName(), expected.getFileName());
        assertEquals(fileInfo.getAccessUrl(), expected.getFileUrl());

        verify(customerRepository).findById(anyString());
        verify(documentServiceClient, times(0)).createOnlyDocument(any(CreateOnlyDocumentRequest.class));
        verify(documentServiceClient).uploadSingleFile(anyString(), any(), anyString());
        verify(requestScope).getCurrentUserId();
    }

    @Test
    @DisplayName("Should throw exception when try to upload customer file for legal entity and trigger injected repository, document service")
    void shouldThrowExceptionWhenUploadCustomerFile() {
        customer.setType(CustomerType.LEGAL.getValue());
        CustomerDocumentType documentType = CustomerDocumentType.SIGNATURE;
        MultipartFile multipartFile = new MockMultipartFile("file", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        IllegalCustomerActionException expected =  assertThrows(IllegalCustomerActionException.class, () -> {
            customerService.uploadCustomerFile(customerId, documentType, multipartFile);
        });

        assertEquals(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_UPLOAD_FILE_ACTION), expected.getMessage());

        verify(customerRepository).findById(anyString());
    }

    @Test
    @DisplayName("Should delete customer file and create document and trigger injected repository, document service")
    void shouldDeleteCustomerFile() {
        CustomerDocumentType documentType = CustomerDocumentType.PHOTO;
        Document document = new Document();
        document.setId("1");
        DocumentResponse response = new DocumentResponse();
        response.setData(document);
        customer.setTypeId(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(requestScope.getCurrentUserId()).thenReturn(userId);
        when(documentServiceClient.removeFile(customer.getNaturalCustomerInfo().getPhotoDocument().getDocumentId(), customer.getNaturalCustomerInfo().getPhotoDocument().getFileId(), userId))
                .thenReturn(response);

        customerService.deleteCustomerFile(customerId, documentType);

        assertNull(customer.getNaturalCustomerInfo().getPhotoDocument().getFileUrl());
        assertNull(customer.getNaturalCustomerInfo().getPhotoDocument().getFileName());
        assertNull(customer.getNaturalCustomerInfo().getPhotoDocument().getFileId());

        verify(customerRepository).findById(anyString());
        verify(documentServiceClient).removeFile(anyString(), anyString(), anyString());
        verify(requestScope).getCurrentUserId();
        verify(customerRepository).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should throw exception when try to delete customer file for legal entity and trigger injected repository, document service")
    void shouldThrowExceptionWhenDeleteCustomerFileForLegalEntity() {
        customer.setType(CustomerType.LEGAL.getValue());
        CustomerDocumentType documentType = CustomerDocumentType.SIGNATURE;

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        IllegalCustomerActionException expected =  assertThrows(IllegalCustomerActionException.class, () -> {
            customerService.deleteCustomerFile(customerId, documentType);
        });

        assertEquals(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_DELETE_FILE_ACTION), expected.getMessage());

        verify(customerRepository).findById(anyString());
    }

    @Test
    @DisplayName("Should throw exception when try to delete customer file and trigger injected repository, document service")
    void shouldThrowExceptionWhenDeleteCustomerFile() {
        customer.setType(CustomerType.NATURAL.getValue());
        customer.getNaturalCustomerInfo().setSignatureSampleDocument(null);
        CustomerDocumentType documentType = CustomerDocumentType.SIGNATURE;

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        IllegalCustomerActionException expected =  assertThrows(IllegalCustomerActionException.class, () -> {
            customerService.deleteCustomerFile(customerId, documentType);
        });

        assertEquals(String.format(ExceptionMessages.CUSTOMER_WRONG_ACTION_MESSAGE, ExceptionMessages.CUSTOMER_DELETE_FILE_ACTION), expected.getMessage());

        verify(customerRepository).findById(anyString());
    }
}