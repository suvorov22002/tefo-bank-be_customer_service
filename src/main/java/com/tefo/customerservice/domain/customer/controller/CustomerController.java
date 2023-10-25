package com.tefo.customerservice.domain.customer.controller;

import com.tefo.customerservice.core.annotation.PreValidateStatusAndPermission;
import com.tefo.customerservice.core.service.CustomerMapperWithCheckedPermissions;
import com.tefo.customerservice.domain.customer.dto.CustomerListResponseDto;
import com.tefo.customerservice.domain.customer.dto.CustomerRequestDto;
import com.tefo.customerservice.domain.customer.dto.CustomerResponseDto;
import com.tefo.customerservice.domain.customer.dto.RejectCustomerDto;
import com.tefo.customerservice.domain.customer.model.CustomerDocument;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerDocumentType;
import com.tefo.customerservice.domain.customer.service.CustomerService;
import com.tefo.library.commonutils.basestructure.dto.BasicInfoDto;
import com.tefo.library.commonutils.constants.RestEndpoints;
import com.tefo.library.commonutils.pagination.PageDto;
import com.tefo.library.commonutils.pagination.PaginationUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(RestEndpoints.CUSTOMER_URL)
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapperWithCheckedPermissions customerMapper;

    @GetMapping("/{customerId}")
    private ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable String customerId) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.getCustomerById(customerId)));
    }

    @GetMapping("/all")
    private ResponseEntity<List<CustomerListResponseDto>> getAllCustomers() {
        return ResponseEntity.ok(customerMapper.toDtoList(customerService.getCustomers()));
    }

    @GetMapping
    private ResponseEntity<PageDto<CustomerListResponseDto>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(PaginationUtils.convertEntityPageToDtoPage(customerService.getCustomersPaginated(pageable),
                customerMapper::toDtoList));
    }

    @GetMapping("/basic-info")
    ResponseEntity<List<BasicInfoDto<String>>> getCustomersBasicInfo() {
        return ResponseEntity.ok(customerMapper.toBasicInfoList(customerService.getCustomers()));
    }

    @GetMapping("/active/basic-info")
    ResponseEntity<List<BasicInfoDto<String>>> getActiveCustomersBasicInfo() {
        return ResponseEntity.ok(customerMapper.toBasicInfoList(customerService.getActiveCustomers()));
    }

    @PostMapping
    private ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody @Valid CustomerRequestDto dto) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.createCustomer(customerMapper.createDtoToEntity(dto))));
    }

    @PreValidateStatusAndPermission(type = "EDIT")
    @PutMapping("/{customerId}")
    private ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable String customerId,
                                                               @RequestBody @Valid CustomerRequestDto dto) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.updateCustomer(customerMapper.updateDtoToEntity(dto), customerId)));
    }

    @DeleteMapping("/{customerId}")
    private ResponseEntity<Void> deleteCustomerById(@PathVariable String customerId) {
        customerService.deleteCustomerById(customerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    private ResponseEntity<Void> deleteCustomerById() {
        customerService.deleteAllCustomer();
        return ResponseEntity.noContent().build();
    }

    @PreValidateStatusAndPermission(type = "REJECT")
    @PostMapping("/{customerId}/reject")
    private ResponseEntity<CustomerResponseDto> rejectCustomer(@PathVariable String customerId,
                                                               @RequestBody @Valid RejectCustomerDto dto) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.rejectCustomer(customerId, dto)));
    }

    @PreValidateStatusAndPermission(type = "VALIDATE")
    @PostMapping("/{customerId}/validate")
    private ResponseEntity<CustomerResponseDto> validateCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.validateCustomer(customerId)));
    }

    @PreValidateStatusAndPermission(type = "AUTHORIZE")
    @PostMapping("/{customerId}/authorize")
    private ResponseEntity<CustomerResponseDto> authorizeCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerMapper.toDto(customerService.authorizeCustomer(customerId)));
    }

    @PostMapping("/{customerId}/upload-file")
    private ResponseEntity<CustomerDocument> uploadCustomerFile(@PathVariable String customerId,
                                                                @RequestParam CustomerDocumentType documentType,
                                                                @RequestPart MultipartFile file) {
        return ResponseEntity.ok(customerService.uploadCustomerFile(customerId, documentType, file));
    }

    @DeleteMapping("/{customerId}/files")
    private ResponseEntity<Void> deleteCustomerFile(@PathVariable String customerId,
                                                    @RequestParam CustomerDocumentType documentType) {
        customerService.deleteCustomerFile(customerId, documentType);
        return ResponseEntity.noContent().build();
    }
}
