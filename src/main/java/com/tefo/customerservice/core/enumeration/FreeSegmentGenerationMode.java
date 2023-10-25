package com.tefo.customerservice.core.enumeration;

public enum FreeSegmentGenerationMode {

    RANDOM("Random"),
    SEQUENTIAL("Sequential");

    private String value;

    FreeSegmentGenerationMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
