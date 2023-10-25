package com.tefo.customerservice.domain.customer.repository;

import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.annotation.Collation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

    @Collation("en")
    Page<CustomerEntity> findAllByOrderByShortName(Pageable pageable);

    @Collation("en")
    List<CustomerEntity> findAllByOrderByShortName();

    @Collation("en")
    List<CustomerEntity> findAllByStatusOrderByShortName(CustomerStatus status);
}
