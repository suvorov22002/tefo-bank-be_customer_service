package com.tefo.customerservice.domain.customer.customfields;

import com.tefo.library.commonutils.constants.RestEndpoints;
import com.tefo.library.customdata.processing.SupportedDatabaseType;
import com.tefo.library.customdata.processing.SupportedIDType;
import com.tefo.library.customdata.processing.annotation.*;

@CustomFieldConfiguration(
        databaseType = SupportedDatabaseType.MONGO,
        idType = SupportedIDType.STRING,
        entityLevelType = SupportedIDType.STRING,
        fieldEntity = @CustomFieldEntity(name = "CustomerCustomFieldEntity", dbAlias = "customer_custom_field"),
        templateEntity = @CustomTemplateEntity(name = "CustomerTemplateEntity", dbAlias = "customer_entity_template", entityStructure = CustomerStructure.class),
        fieldRepository = @CustomFieldRepository(name = "CustomerFieldRepository"),
        templateRepository = @CustomTemplateRepository(name = "CustomerTemplateRepository"),
        fieldService = @CustomFieldService(name = "CustomerFieldService"),
        templateService = @CustomTemplateService(name = "CustomerTemplateService"),
        fieldController = @CustomFieldController(name = "CustomerFieldController", requestMapping = RestEndpoints.CUSTOMER_URL + "/custom-fields"),
        templateController = @CustomTemplateController(name = "CustomerTemplateController", requestMapping = RestEndpoints.CUSTOMER_URL + "/templates"),
        fieldValueBuilder = @CustomFieldValueBuilder(name = "CustomerFieldValueBuilder"),
        fieldValueValidator = @CustomFieldValueValidator(name = "CustomerFieldValueValidator"),
        fieldGroupEntity = @CustomFieldGroupEntity(name = "CustomerFieldGroupEntity", dbAlias = "customer_custom_field_group"),
        fieldGroupRepository = @CustomFieldGroupRepository(name = "CustomerFieldGroupRepository"),
        fieldGroupService = @CustomFieldGroupService(name = "CustomerFieldGroupService"),
        fieldGroupController = @CustomFieldGroupController(name = "CustomerFieldGroupController", requestMapping = RestEndpoints.CUSTOMER_URL + "/custom-groups")
)
public interface CustomerCustomFieldInitializer {
}
