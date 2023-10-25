package com.tefo.customerservice.domain.customer.customfields;

import com.tefo.customerservice.core.dto.BankProfileDTO;
import com.tefo.customerservice.core.dto.CustomerSettingsDTO;
import com.tefo.customerservice.core.dto.UserPermissionBasicDto;
import com.tefo.customerservice.core.dto.UserResponseDto;
import com.tefo.customerservice.core.feginclient.CoreSettingsServiceClient;
import com.tefo.customerservice.core.service.UserServiceImpl;
import com.tefo.customerservice.domain.customer.model.CustomerEntity;
import com.tefo.customerservice.domain.customer.model.enumeration.CustomerPermissionCode;
import com.tefo.customerservice.domain.customer.validations.CustomerValidationRules;
import com.tefo.library.commonutils.auth.RequestScope;
import com.tefo.library.commonutils.constants.RestEndpoints;
import com.tefo.library.commonutils.constants.SystemDictionaryConstants;
import com.tefo.library.commonutils.constants.ValidationMessages;
import com.tefo.library.customdata.field.FieldBuilder;
import com.tefo.library.customdata.field.instance.Field;
import com.tefo.library.customdata.field.validation.ValidationErrorMessages;
import com.tefo.library.customdata.field.validation.instance.*;
import com.tefo.library.customdata.field.value.instance.date.PeriodTerm;
import com.tefo.library.customdata.group.dto.GroupDataDTO;
import com.tefo.library.customdata.group.entity.GroupEntityAppearance;
import com.tefo.library.customdata.template.EntityStructure;

import java.util.Map;
import java.util.Set;

public class CustomerStructure implements EntityStructure {

    public static final String CUSTOMER_TYPE_CODE = "typeId";
    public static final String CUSTOMER_TYPE_LABEL = "Type";
    public static final String CUSTOMER_GROUP_NAME = "Name";
    public static final String CUSTOMER_GROUP_NAME_CODE = "name";
    public static final String CUSTOMER_GROUP_DETAILS = "Details";
    public static final String CUSTOMER_GROUP_DETAILS_CODE = "details";
    public static final String CUSTOMER_GROUP_SEGMENTATION = "Segmentation";
    public static final String CUSTOMER_GROUP_SEGMENTATION_CODE = "segmentation";
    public static final String CUSTOMER_GROUP_KYC = "KYC";
    public static final String CUSTOMER_GROUP_KYC_CODE = "kyc";
    public static final String CUSTOMER_GROUP_OTHER = "Other";
    public static final String CUSTOMER_GROUP_OTHER_CODE = "other";
    private static final String ID_FIELD_NAME = "id";
    private static final String NAME_FIELD = "name";
    private static final String UNIT_KEY = "unit";
    private static final String TERM_KEY = "term";

    private static final RequiredValidationRule REQUIRED_VALIDATION_RULE = new RequiredValidationRule(ValidationMessages.VALUE_SHOULD_NOT_BE_NULL);

    @Override
    public Set<Field> getFields() {
        return Set.of(
                createTitleSalutationField(),
                createFirstNameField(),
                createMiddleNameField(),
                createLastNameField(),
                createMaidenNameField(),
                createFathersFirstNameField(),
                createFathersMiddleNameField(),
                createFathersLastNameField(),
                createMothersFirstNameField(),
                createMothersMiddleNameField(),
                createMothersLastNameField(),
                createLegalNameField(),
                createShortNameField(),
                createNameToReturnField(),
                createCountryField(),
                createGenderField(),
                createBirthDateField(),
                createBirthDatePrecisionField(),
                createCountryOfBirthField(),
                createRegionOfBirthField(),
                createPlaceOfBirthField(),
                createLanguageField(),
                createEducationField(),
                createMaritalStatusField(),
                createNumberOfDependentPersonsField(),
                createEnregistrationDateField(),
                createRegistrationNumberField(),
                createCountryOfRiskField(),
                createFinancialInstitutionField(),
                createBankField(),
                createBicField(),
                createLegalFormForNaturalPersonField(),
                createLegalFormForLegalEntityField(),
                createEconomicSectorForNaturalPersonField(),
                createEconomicSectorForLegalEntityField(),
                createRiskClassField(),
                createRiskClassAdjustedField(),
                createSegmentField(),
                createPortfolioField(),
                createCostCenterField(),
                createProfitCenterField(),
                createUnitField(),
                createRelationshipManagerField(),
                createFirstContactDateField(),
                createIntroducerField(),
                createSponsorField(),
                createKycStatusField(),
                createPreviousReviewDateField(),
                createNextReviewDateField(),
                createMonitoringMondeField(),
                createBankEmployeeField(),
                createUsernameField(),
                createMasterCodeField(),
                createRelationshipEndDateField(),
                createDeceasedDateField(),
                createBankruptcyDateField(),
                createNotesField()
        );
    }

    @Override
    public Set<Field> getEntityLevelFields() {
        return Set.of(createCustomerTypeField());
    }

    private Field createCustomerTypeField() {
        return FieldBuilder.dictionaryField(CUSTOMER_TYPE_CODE, CUSTOMER_TYPE_LABEL, RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_TYPE_DICTIONARY_CODE)
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .order(0)
                .visible(true)
                .required()
                .build();
    }

    private Field createTitleSalutationField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.salutationId", "Title/Salutation", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_TITLE_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(1)
                .visible(true)
                .build();
    }

    private Field createFirstNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.firstName", "First Name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(2)
                .visible(true)
                .build();
    }

    private Field createMiddleNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.middleName", "Middle Name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(3)
                .visible(true)
                .build();
    }

    private Field createLastNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.lastName", "Last Name")
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(4)
                .required()
                .visible(true)
                .build();
    }

    private Field createMaidenNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.maidenName", "Maiden Name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(5)
                .visible(true)
                .build();
    }

    private Field createFathersFirstNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.fathersFirstName", "Father's first name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(6)
                .visible(true)
                .build();
    }

    private Field createFathersMiddleNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.fathersMiddleName", "Father's middle name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(7)
                .visible(true)
                .build();
    }

    private Field createFathersLastNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.fathersLastName", "Father's last name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(8)
                .visible(true)
                .build();
    }

    private Field createMothersFirstNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.mothersFirstName", "Mother's first name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(9)
                .visible(true)
                .build();
    }

    private Field createMothersMiddleNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.mothersMiddleName", "Mother's middle name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(10)
                .visible(true)
                .build();
    }

    private Field createMothersLastNameField() {
        return FieldBuilder.textField("naturalCustomerInfo.parentsInfo.mothersLastName", "Mother's last name")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(11)
                .visible(true)
                .build();
    }

    private Field createLegalNameField() {
        return FieldBuilder.textField("legalCustomerInfo.legalName", "Legal Name")
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .addValidationRule(new MinLengthValidationRule(1, String.format(ValidationMessages.MIN_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_NAME_MIN), CustomerValidationRules.CUSTOMER_NAME_MIN))
                .addValidationRule(new MaxLengthValidationRule(2, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LEGAL_NAME_MAX), CustomerValidationRules.CUSTOMER_LEGAL_NAME_MAX))
                .addValidationRule(new RegexpValidationRule(3, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_LEGAL_NAME_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_LEGAL_NAME_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(12)
                .required()
                .visible(true)
                .build();
    }

    private Field createShortNameField() {
        return FieldBuilder.textField(CustomerEntity.Fields.shortName, "Short Name")
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .addValidationRule(new MinLengthValidationRule(1, String.format(ValidationMessages.MIN_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_NAME_MIN), CustomerValidationRules.CUSTOMER_NAME_MIN))
                .addValidationRule(new MaxLengthValidationRule(2, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_SHORT_NAME_MAX), CustomerValidationRules.CUSTOMER_SHORT_NAME_MAX))
                .addValidationRule(new RegexpValidationRule(3, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(13)
                .required()
                .visible(true)
                .build();
    }

    private Field createNameToReturnField() {
        return FieldBuilder.textField(CustomerEntity.Fields.nameToReturn, "Name To Return")
                .group(CUSTOMER_GROUP_NAME_CODE)
                .order(14)
                .visible(true)
                .build();
    }

    private Field createCountryField() {
        return FieldBuilder.remoteEntitiesField(CustomerEntity.Fields.countryId, "Country", RestEndpoints.COUNTRY_URL + "/active/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                // TODO: set default value from bank profile
                .defaultValue(186)
                .order(15)
                .alias(CustomerEntity.Fields.countryId)
                .required()
                .visible(true)
                .build();
    }

    private Field createGenderField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.genderId", "Gender", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.GENDER_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(16)
                .visible(true)
                .build();
    }

    private Field createBirthDateField() {
        return FieldBuilder.dateField("naturalCustomerInfo.birthDetails.birthDate", "Birth Date")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .addValidationRule(CoreSettingsServiceClient.class, coreSettingsServiceClient -> {
                    Integer minAgeForOnboarding = coreSettingsServiceClient.getPartOfSettings().getMinAgeForOnboarding();
                    return new MinPastValidationRule(1, String.format(ValidationErrorMessages.MIN_PAST_ERROR_MESSAGE,
                            minAgeForOnboarding, PeriodTerm.YEAR), Map.of(UNIT_KEY, coreSettingsServiceClient.getPartOfSettings().getMinAgeForOnboarding(), TERM_KEY, PeriodTerm.YEAR.toString()));
                })
                .addValidationRule(new MaxPastValidationRule(2, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 100, PeriodTerm.YEAR.toString()), Map.of(UNIT_KEY, 100, TERM_KEY, PeriodTerm.YEAR.toString())))
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(17)
                .visible(true)
                .build();
    }

    private Field createBirthDatePrecisionField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.birthDetails.birthDatePrecisionId", "Birth Date Precision", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_BIRTH_DATE_PRECISION_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(18)
                .visible(true)
                .build();
    }

    private Field createCountryOfBirthField() {
        return FieldBuilder.remoteEntitiesField("naturalCustomerInfo.birthDetails.countryOfBirthId", "Country Of Birth", RestEndpoints.COUNTRY_URL + "/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(19)
                .visible(true)
                .build();
    }

    private Field createRegionOfBirthField() {
        return FieldBuilder.textField("naturalCustomerInfo.birthDetails.regionOfBirth", "Region Of Birth")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(20)
                .visible(true)
                .build();
    }

    private Field createPlaceOfBirthField() {
        return FieldBuilder.textField("naturalCustomerInfo.birthDetails.placeOfBirth", "Place Of Birth")
                .addValidationRule(new MaxLengthValidationRule(1, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MAX), CustomerValidationRules.CUSTOMER_LENGTH_MAX))
                .addValidationRule(new RegexpValidationRule(2, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(21)
                .visible(true)
                .build();
    }

    private Field createLanguageField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.languageId", "Language", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.INTERFACE_LANGUAGE_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(22)
                .visible(true)
                .build();
    }

    private Field createEducationField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.educationId", "Education", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.EDUCATION_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(23)
                .visible(true)
                .build();
    }

    private Field createMaritalStatusField() {
        return FieldBuilder.dictionaryField("naturalCustomerInfo.maritalStatusId", "Marital status", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_MARITAL_STATUS_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(24)
                .visible(true)
                .build();
    }

    private Field createNumberOfDependentPersonsField() {
        return FieldBuilder.integerField("naturalCustomerInfo.numberOfDependentPersons", "Number of dependent persons")
                .addValidationRule(new MinValidationRule(1, String.format(ValidationMessages.MIN_VALIDATION, CustomerValidationRules.CUSTOMER_LENGTH_MIN), CustomerValidationRules.CUSTOMER_LENGTH_MIN))
                .addValidationRule(new MaxValidationRule(2, String.format(ValidationMessages.MAX_VALIDATION, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(25)
                .visible(true)
                .build();
    }

    private Field createEnregistrationDateField() {
        return FieldBuilder.dateField("legalCustomerInfo.enregistrationDate", "Enregistration date")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(26)
                .visible(true)
                .build();
    }

    private Field createRegistrationNumberField() {
        return FieldBuilder.textField("legalCustomerInfo.registrationNumber", "Registration number")
                .addValidationRule(new MinLengthValidationRule(1, String.format(ValidationMessages.MIN_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_NAME_MIN), CustomerValidationRules.CUSTOMER_NAME_MIN))
                .addValidationRule(new MaxLengthValidationRule(2, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_LENGTH_MID), CustomerValidationRules.CUSTOMER_LENGTH_MID))
                .addValidationRule(new RegexpValidationRule(3, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_REGISTRATION_NUMBER_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_REGISTRATION_NUMBER_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(27)
                .visible(true)
                .build();
    }

    private Field createCountryOfRiskField() {
        return FieldBuilder.remoteEntitiesField("legalCustomerInfo.countryOfRiskId", "Country of risk", RestEndpoints.COUNTRY_URL + "/active/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(28)
                .visible(true)
                .build();
    }

    private Field createFinancialInstitutionField() {
        return FieldBuilder.booleanField("legalCustomerInfo.financialInstitution", "Financial institution")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(29)
                .defaultValue(false)
                .visible(true)
                .build();
    }

    private Field createBankField() {
        return FieldBuilder.booleanField("legalCustomerInfo.bank", "Bank")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(30)
                .defaultValue(false)
                .visible(true)
                .build();
    }

    private Field createBicField() {
        return FieldBuilder.textField("legalCustomerInfo.bic", "BIC (SWIFT Code)")
                .addValidationRule(new MinLengthValidationRule(1, String.format(ValidationMessages.MIN_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_BIC_MIN), CustomerValidationRules.CUSTOMER_BIC_MIN))
                .addValidationRule(new MaxLengthValidationRule(2, String.format(ValidationMessages.MAX_VALIDATION_STRING, CustomerValidationRules.CUSTOMER_BIC_MAX), CustomerValidationRules.CUSTOMER_BIC_MAX))
                .addValidationRule(new RegexpValidationRule(3, String.format(ValidationMessages.ALLOWED_SYMBOLS_VALIDATION, CustomerValidationRules.CUSTOMER_BIC_ALLOWED_CHARACTERS), CustomerValidationRules.CUSTOMER_BIC_REGEX))
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_DETAILS_CODE)
                .order(31)
                .visible(true)
                .build();
    }

    private Field createLegalFormForNaturalPersonField() {
        return FieldBuilder.dictionaryField("classification.legalFormId", "Legal form", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_LEGAL_FORM_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .defaultValue(CoreSettingsServiceClient.class, coreSettingsServiceClient -> {
                    CustomerSettingsDTO partOfSettings = coreSettingsServiceClient.getPartOfSettings();
                    return partOfSettings.getDefaultLegalFormForNaturalPersonDictionaryValueId();
                })
                .order(32)
                .visible(true)
                .build();
    }

    private Field createLegalFormForLegalEntityField() {
        return FieldBuilder.dictionaryField("legalFormIdLegal", "Legal form", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_LEGAL_FORM_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .alias("classification.legalFormId")
                .order(33)
                .visible(true)
                .build();
    }

    private Field createEconomicSectorForNaturalPersonField() {
        return FieldBuilder.dictionaryField("classification.economicSectorId", "Economic sector", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_ECONOMIC_SECTOR_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .defaultValue(CoreSettingsServiceClient.class, coreSettingsServiceClient -> {
                    CustomerSettingsDTO partOfSettings = coreSettingsServiceClient.getPartOfSettings();
                    return partOfSettings.getDefaultEconomicSectorForNaturalPersonDictionaryValueId();
                })
                .order(34)
                .visible(true)
                .build();
    }

    private Field createEconomicSectorForLegalEntityField() {
        return FieldBuilder.dictionaryField("economicSectorIdLegal", "Economic sector", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_ECONOMIC_SECTOR_DICTIONARY_CODE)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_LEGAL_ENTITY_VALUE_ID)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .alias("classification.economicSectorId")
                .order(35)
                .visible(true)
                .build();
    }

    private Field createRiskClassField() {
        return FieldBuilder.dictionaryField("classification.riskClassId", "Risk Class", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_RISK_CLASSIFICATION_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(36)
                .visible(true)
                .build();
    }

    private Field createRiskClassAdjustedField() {
        return FieldBuilder.dictionaryField("classification.riskClassAdjustedId", "Risk Class Adjusted", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_RISK_CLASSIFICATION_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(37)
                .visible(true)
                .build();
    }

    private Field createSegmentField() {
        return FieldBuilder.dictionaryField("classification.segmentId", "Segment", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_SEGMENT_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(38)
                .visible(true)
                .build();
    }

    private Field createPortfolioField() {
        return FieldBuilder.dictionaryField("classification.portfolioId", "Portfolio", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_PORTFOLIO_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(39)
                .visible(true)
                .build();
    }

    private Field createCostCenterField() {
        //TODO: make dictionary after TBD will be ready
        return FieldBuilder.textField("classification.costCenterId", "Cost Center")
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(40)
                .visible(true)
                .build();
    }

    private Field createProfitCenterField() {
        //TODO: make dictionary after TBD will be ready
        return FieldBuilder.textField("classification.profitCenterId", "Profit Center")
                .group(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .order(41)
                .visible(true)
                .build();
    }

    private Field createUnitField() {
        return FieldBuilder.remoteEntitiesField(CustomerEntity.Fields.unitId, "Unit", RestEndpoints.UNITS + "/active/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .defaultValue(UserServiceImpl.class, UserServiceImpl::getUserUnit)
                .order(42)
                .required()
                .visible(true)
                .build();
    }

    private Field createRelationshipManagerField() {
        return FieldBuilder.remoteEntitiesField("kyc.relationshipManagerId", "Relationship manager", RestEndpoints.USERS + "/all/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .addValidationRule(REQUIRED_VALIDATION_RULE)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .defaultValue(RequestScope.class, RequestScope::getCurrentUserId)
                .disabled(UserServiceImpl.class, userService ->
                        userService.getUserPermission().stream()
                                .map(UserPermissionBasicDto::getCode)
                                .toList().contains(CustomerPermissionCode.EDIT_CUSTOMER_RM.name())
                )
                .order(43)
                .required()
                .visible(true)
                .build();
    }

    private Field createFirstContactDateField() {
        return FieldBuilder.dateField("kyc.firstContactDate", "First contact date")
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(44)
                .visible(true)
                .build();
    }

    private Field createIntroducerField() {
        return FieldBuilder.remoteEntitiesField("kyc.introducerId", "Introducer", RestEndpoints.CUSTOMER_URL + "/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(45)
                .visible(true)
                .build();
    }

    private Field createSponsorField() {
        return FieldBuilder.booleanField("kyc.sponsor", "Sponsor")
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(46)
                .defaultValue(false)
                .visible(true)
                .build();
    }

    private Field createKycStatusField() {
        return FieldBuilder.dictionaryField("kyc.kycStatusId", "KYC Status", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.KYC_STATUS_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(47)
                .visible(true)
                .build();
    }

    private Field createPreviousReviewDateField() {
        return FieldBuilder.dateField("kyc.previousReviewDate", "Previous review date")
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(48)
                .visible(true)
                .build();
    }

    private Field createNextReviewDateField() {
        return FieldBuilder.dateField("kyc.nextReviewDate", "Next review date")
                .addValidationRule(new MinFutureValidationRule(1, String.format(ValidationErrorMessages.MIN_FUTURE_ERROR_MESSAGE, 1, PeriodTerm.DAY), Map.of(UNIT_KEY, 1, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(49)
                .visible(true)
                .build();
    }

    private Field createMonitoringMondeField() {
        return FieldBuilder.dictionaryField("kyc.monitoringModeId", "Monitoring Mode", RestEndpoints.DICTIONARY_URL + "/values/basic-info?dictionaryCode=" + SystemDictionaryConstants.CUSTOMER_MONITORING_MODE_DICTIONARY_CODE)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(50)
                .visible(true)
                .build();
    }

    private Field createBankEmployeeField() {
        return FieldBuilder.booleanField("naturalCustomerInfo.bankEmployee", "Bank Employee")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(51)
                .defaultValue(false)
                .visible(true)
                .build();
    }

    private Field createUsernameField() {
        return FieldBuilder.remoteEntitiesField("naturalCustomerInfo.userId", "Username", RestEndpoints.USERS + "/active/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .group(CUSTOMER_GROUP_KYC_CODE)
                .order(52)
                .visible(true)
                .build();
    }

    private Field createMasterCodeField() {
        return FieldBuilder.remoteEntitiesField(CustomerEntity.Fields.masterId, "Master Code", RestEndpoints.CUSTOMER_URL + "/active/basic-info",
                        NAME_FIELD, ID_FIELD_NAME)
                .group(CUSTOMER_GROUP_OTHER_CODE)
                .order(53)
                .visible(true)
                .build();
    }

    private Field createRelationshipEndDateField() {
        return FieldBuilder.dateField(CustomerEntity.Fields.relationshipEndDate, "Relationship end date")
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_OTHER_CODE)
                .order(54)
                .visible(true)
                .build();
    }

    private Field createDeceasedDateField() {
        return FieldBuilder.dateField("naturalCustomerInfo.birthDetails.deceasedDate", "Deceased date")
                .entityLevelValidation(SystemDictionaryConstants.CUSTOMER_NATURAL_TYPE_VALUE_ID)
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_OTHER_CODE)
                .order(55)
                .visible(true)
                .build();
    }

    private Field createBankruptcyDateField() {
        return FieldBuilder.dateField(CustomerEntity.Fields.bankruptcyDate, "Bankruptcy date")
                .addValidationRule(new MaxPastValidationRule(1, String.format(ValidationErrorMessages.MAX_PAST_ERROR_MESSAGE, 0, PeriodTerm.DAY), Map.of(UNIT_KEY, 0, TERM_KEY, PeriodTerm.DAY.toString())))
                .group(CUSTOMER_GROUP_OTHER_CODE)
                .order(56)
                .visible(true)
                .build();
    }

    private Field createNotesField() {
        return FieldBuilder.textField(CustomerEntity.Fields.notes, "Notes")
                .group(CUSTOMER_GROUP_OTHER_CODE)
                .order(57)
                .visible(true)
                .build();
    }

    @Override
    public Set<GroupDataDTO> getPrimaryFieldGroups() {
        return Set.of(
                createNameGroup(),
                createDetailsGroup(),
                createSegmentationGroup(),
                createKycGroup(),
                createOtherGroup()
        );
    }

    private GroupDataDTO createNameGroup() {
        return GroupDataDTO.builder()
                .code(CUSTOMER_GROUP_NAME_CODE)
                .name(CUSTOMER_GROUP_NAME)
                .label(CUSTOMER_GROUP_NAME)
                .appearance(GroupEntityAppearance.EXPANDED)
                .index(1)
                .build();
    }

    private GroupDataDTO createDetailsGroup() {
        return GroupDataDTO.builder()
                .code(CUSTOMER_GROUP_DETAILS_CODE)
                .name(CUSTOMER_GROUP_DETAILS)
                .label(CUSTOMER_GROUP_DETAILS)
                .appearance(GroupEntityAppearance.EXPANDED)
                .index(2)
                .build();
    }

    private GroupDataDTO createSegmentationGroup() {
        return GroupDataDTO.builder()
                .code(CUSTOMER_GROUP_SEGMENTATION_CODE)
                .name(CUSTOMER_GROUP_SEGMENTATION)
                .label(CUSTOMER_GROUP_SEGMENTATION)
                .appearance(GroupEntityAppearance.EXPANDED)
                .index(3)
                .build();
    }

    private GroupDataDTO createKycGroup() {
        return GroupDataDTO.builder()
                .code(CUSTOMER_GROUP_KYC_CODE)
                .name(CUSTOMER_GROUP_KYC)
                .label(CUSTOMER_GROUP_KYC)
                .appearance(GroupEntityAppearance.EXPANDED)
                .index(4)
                .build();
    }

    private GroupDataDTO createOtherGroup() {
        return GroupDataDTO.builder()
                .code(CUSTOMER_GROUP_OTHER_CODE)
                .name(CUSTOMER_GROUP_OTHER)
                .label(CUSTOMER_GROUP_OTHER)
                .appearance(GroupEntityAppearance.EXPANDED)
                .index(5)
                .build();
    }
}