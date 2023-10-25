package com.tefo.customerservice.core.utils;

import java.util.Objects;
import java.util.stream.Stream;

public class UtilMethods {

    public static boolean validateProperties(Object... properties) {
        return Stream.of(properties)
                .noneMatch(Objects::isNull);
    }
}
