package com.tefo.customerservice.domain.customer.validations;

public final class CustomerValidationRules {

    public static final int CUSTOMER_LENGTH_MAX = 50;
    public static final int CUSTOMER_LENGTH_MID = 20;
    public static final int CUSTOMER_LENGTH_MIN = 0;
    public static final int CUSTOMER_NAME_MIN = 5;
    public static final int CUSTOMER_BIC_MIN = 8;
    public static final int CUSTOMER_BIC_MAX = 11;
    public static final int CUSTOMER_LEGAL_NAME_MAX = 300;
    public static final int CUSTOMER_SHORT_NAME_MAX = 200;
    public static final String CUSTOMER_ALLOWED_CHARACTERS = "A-Za-z .-'";
    public static final String CUSTOMER_REGEX = "^[A-Za-z .\\-']*$";
    public static final String CUSTOMER_LEGAL_NAME_ALLOWED_CHARACTERS = "A-Za-z0-9 .-'/";
    public static final String CUSTOMER_LEGAL_NAME_REGEX = "^[A-Za-z0-9 .\\-'\\/]+$";
    public static final String CUSTOMER_REGISTRATION_NUMBER_ALLOWED_CHARACTERS = "A-Za-z0-9 .-:/#";
    public static final String CUSTOMER_REGISTRATION_NUMBER_REGEX = "^[A-Za-z0-9 .\\-:\\/#]*$";
    public static final String CUSTOMER_BIC_ALLOWED_CHARACTERS = "A-Z0-9";
    public static final String CUSTOMER_BIC_REGEX = "^[A-Z0-9]*$";
}
