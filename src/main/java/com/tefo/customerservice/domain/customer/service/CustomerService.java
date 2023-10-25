package com.tefo.customerservice.domain.customer.service;

import com.tefo.customerservice.domain.customer.dto.RejectCustomerDto;
import com.tefo.customerservice.domain.customer.model.CustomerDocument;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerDocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService {

    List<CustomerEntity> getCustomers();
    List<CustomerEntity> getActiveCustomers();

    Page<CustomerEntity> getCustomersPaginated(Pageable pageable);

    CustomerEntity getCustomerById(String id);

    CustomerEntity createCustomer(CustomerEntity customer);

    CustomerEntity updateCustomer(CustomerEntity customer, String customerId);

    void deleteCustomerById(String id);

    void deleteAllCustomer();

    CustomerEntity rejectCustomer(String customerId, RejectCustomerDto dto);
    CustomerEntity validateCustomer(String customerId);
    CustomerEntity authorizeCustomer(String customerId);

    CustomerDocument uploadCustomerFile(String customerId, CustomerDocumentType documentType, MultipartFile file);
    void deleteCustomerFile(String customerId, CustomerDocumentType documentType);
}
