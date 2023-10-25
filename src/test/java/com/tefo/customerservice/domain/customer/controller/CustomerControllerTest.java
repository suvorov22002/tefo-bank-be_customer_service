package com.tefo.customerservice.domain.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tefo.customerservice.RequestScopeImpl;
import com.tefo.customerservice.core.interceptor.StatusPermissionValidationHandlerInterceptor;
import com.tefo.customerservice.core.service.CustomerMapperWithCheckedPermissions;
import com.tefo.customerservice.core.service.UserService;
import com.tefo.customerservice.domain.customer.dto.CustomerListResponseDto;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.CustomerResponseDto;
import com.tefo.customerservice.domain.customer.dto.RejectCustomerDto;
import com.tefo.customerservice.domain.customer.model.CustomerDocument;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.KYC;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerDocumentType;
import com.tefo.customerservice.domain.customer.service.CustomerService;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(InstancioExtension.class)
@WebMvcTest(CustomerController.class)
@MockBeans({@MockBean(UserService.class), @MockBean(RequestScopeImpl.class)})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private CustomerMapperWithCheckedPermissions customerMapper;
    @MockBean
    private StatusPermissionValidationHandlerInterceptor interceptor;

    private CustomerEntity customer;
    private CustomerResponseDto customerResponseDto;
    private CustomerRequestDto customerRequestDto;
    private CustomerListResponseDto customerListResponseDto;
    private BasicInfoDto<String> basicInfoDto;
    private List<CustomerEntity> customers;
    private String customerId;

    @BeforeEach
    void setUp() {
        customer = Instancio.create(CustomerEntity.class);
        customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setId("1");
        customerResponseDto.setCode("code");

        customerListResponseDto = Instancio.create(CustomerListResponseDto.class);
        customerListResponseDto.setId("id");

        basicInfoDto = new BasicInfoDto<>();

        customerRequestDto = new CustomerRequestDto();
        KYC kyc = new KYC();
        kyc.setRelationshipManagerId("1");
        customerRequestDto.setTypeId(1);
        customerRequestDto.setCountryId(1);
        customerRequestDto.setUnitId("1");
        customerRequestDto.setKyc(kyc);

        customerId = "customerId";
        customers = List.of(customer);

        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("Should return customer by id and trigger injected service and mapper")
    void shouldReturnCustomerById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(RestEndpoints.CUSTOMER_URL + "/{customerId}", customerId);

        when(customerService.getCustomerById(customerId)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerService).getCustomerById(anyString());
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should return all customers and trigger injected service and mapper")
    void shouldReturnAllCustomers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(RestEndpoints.CUSTOMER_URL + "/all");

        when(customerService.getCustomers()).thenReturn(customers);
        when(customerMapper.toDtoList(customers)).thenReturn(List.of(customerListResponseDto));

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(customerListResponseDto)), content);

        verify(customerService).getCustomers();
        verify(customerMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Should return all customers for given page and trigger injected service and mapper")
    void shouldReturnAllCustomersPageable() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(RestEndpoints.CUSTOMER_URL)
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON);

        when(customerService.getCustomersPaginated(any(Pageable.class))).thenReturn(new PageImpl<>(customers));
        when(customerMapper.toDtoList(customers)).thenReturn(List.of(customerListResponseDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limit").value(1))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.data[0].id").value(customerListResponseDto.getId()));

        verify(customerService).getCustomersPaginated(any(Pageable.class));
        verify(customerMapper).toDtoList(anyList());
    }

    @Test
    @DisplayName("Should return customers basicInfo - id and name and trigger injected service and mapper")
    void shouldReturnBasicCustomerInfo() throws Exception {
        String id = "id";
        String name = "name";
        basicInfoDto.setId(id);
        basicInfoDto.setName(name);

        MockHttpServletRequestBuilder requestBuilder = get(RestEndpoints.CUSTOMER_URL + "/basic-info");

        when(customerService.getCustomers()).thenReturn(customers);
        when(customerMapper.toBasicInfoList(customers)).thenReturn(List.of(basicInfoDto));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name));

        verify(customerService).getCustomers();
        verify(customerMapper).toBasicInfoList(anyList());
    }

    @Test
    @DisplayName("Should create customer and trigger injected service and mapper")
    void shouldCreateCustomer() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(RestEndpoints.CUSTOMER_URL)
                .content(objectMapper.writeValueAsString(customerRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(customerMapper.createDtoToEntity(any(CustomerRequestDto.class))).thenReturn(customer);
        when(customerService.createCustomer(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerMapper).createDtoToEntity(any(CustomerRequestDto.class));
        verify(customerService).createCustomer(any(CustomerEntity.class));
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should update customer and trigger injected service and mapper")
    void shouldUpdateCustomer() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = put(RestEndpoints.CUSTOMER_URL + "/{customerId}", customerId)
                .content(objectMapper.writeValueAsString(customerRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        when(customerMapper.updateDtoToEntity(any(CustomerRequestDto.class))).thenReturn(customer);
        when(customerService.updateCustomer(customer, customerId)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerMapper).updateDtoToEntity(any(CustomerRequestDto.class));
        verify(customerService).updateCustomer(any(CustomerEntity.class), anyString());
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should delete customer by id and trigger injected service")
    void shouldDeleteCustomerById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(RestEndpoints.CUSTOMER_URL + "/{customerId}", customerId);

        doNothing().when(customerService).deleteCustomerById(customerId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(anyString());
    }

    @Test
    @DisplayName("Should delete all customers and trigger injected service")
    void shouldDeleteAllCustomers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(RestEndpoints.CUSTOMER_URL + "/all");

        doNothing().when(customerService).deleteAllCustomer();

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(customerService).deleteAllCustomer();
    }

    @Test
    @DisplayName("Should reject customer and trigger injected service and mapper")
    void shouldRejectCustomer() throws Exception {
        RejectCustomerDto rejectCustomerDto = new RejectCustomerDto();
        rejectCustomerDto.setRejectReasonId(1);
        rejectCustomerDto.setRejectComment("comment");

        MockHttpServletRequestBuilder requestBuilder = post(RestEndpoints.CUSTOMER_URL + "/{customerId}/reject", customerId)
                .content(objectMapper.writeValueAsString(rejectCustomerDto))
                .contentType(MediaType.APPLICATION_JSON);

        when(customerService.rejectCustomer(eq(customerId), any(RejectCustomerDto.class))).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerService).rejectCustomer(anyString(), any(RejectCustomerDto.class));
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should validate customer and trigger injected service and mapper")
    void shouldValidateCustomer() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(RestEndpoints.CUSTOMER_URL + "/{customerId}/validate", customerId);

        when(customerService.validateCustomer(customerId)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerService).validateCustomer(anyString());
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should authorize customer and trigger injected service and mapper")
    void shouldAuthorizeCustomer() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(RestEndpoints.CUSTOMER_URL + "/{customerId}/authorize", customerId);

        when(customerService.authorizeCustomer(customerId)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerResponseDto);

        String content = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(customerResponseDto), content);

        verify(customerService).authorizeCustomer(anyString());
        verify(customerMapper).toDto(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should upload file and trigger injected service and mapper")
    void shouldUploadFile() throws Exception {
        CustomerDocumentType documentType = CustomerDocumentType.PHOTO;
        MockMultipartFile file = new MockMultipartFile("file", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes());
        CustomerDocument document = new CustomerDocument("1", CustomerDocumentType.PHOTO, "", "", "");

        when(customerService.uploadCustomerFile(customerId, documentType, file)).thenReturn(document);

        String content = mockMvc.perform(MockMvcRequestBuilders.multipart(RestEndpoints.CUSTOMER_URL + "/" + customerId + "/upload-file")
                        .file(file)
                        .param("documentType", String.valueOf(CustomerDocumentType.PHOTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        assertEquals(objectMapper.writeValueAsString(document), content);

        verify(customerService).uploadCustomerFile(anyString(), any(CustomerDocumentType.class), any());
    }

    @Test
    @DisplayName("Should remove customer file and trigger injected service and mapper")
    void shouldRemoveFile() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = delete(RestEndpoints.CUSTOMER_URL + "/{customerId}/files", customerId)
                .param("documentType", String.valueOf(CustomerDocumentType.PHOTO));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerFile(anyString(), any(CustomerDocumentType.class));
    }
}