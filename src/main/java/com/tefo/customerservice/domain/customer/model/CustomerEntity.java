package com.tefo.customerservice.domain.customer.model;

import com.tefo.customerservice.core.utils.BaseEntity;
import com.tefo.library.commonutils.validation.MandatoryField;
import com.tefo.library.customdata.TemplatedEntity;
import com.tefo.library.customdata.field.value.instance.FieldValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldNameConstants
@Document(collection = "customers")
public class CustomerEntity extends BaseEntity implements TemplatedEntity {
    @Id
    private String id;
    private String masterId;
    @MandatoryField
    private Integer typeId;
    @MandatoryField
    private String type;
    @MandatoryField
    private String code;
    @MandatoryField
    private Integer statusId;
    @MandatoryField
    private String status;
    @MandatoryField
    private String shortName;
    private String nameToReturn;
    @MandatoryField
    private Integer countryId;
    @MandatoryField
    private String unitId;
    private Classification classification;
    private KYC kyc;
    private AML aml;
    private LocalDate bankruptcyDate;
    private LocalDate relationshipEndDate;
    private String notes;
    private Integer rejectReasonId;
    private String rejectComment;
    private String rejectedBy;
    private LocalDateTime rejectedAt;
    private LegalCustomer legalCustomerInfo;
    private NaturalCustomer naturalCustomerInfo;
    Set<FieldValue<?>> customFields = new HashSet<>();
}
