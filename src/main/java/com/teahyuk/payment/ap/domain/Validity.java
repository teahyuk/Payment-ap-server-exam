package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@EqualsAndHashCode
public class Validity {
    private final static String INVALID_FORMAT = "Create validity error, validity must follow this format %s. validity=%s";
    private final static String VALIDITY_FORMAT = "MMyy";
    private final YearMonth validity;

    public Validity(String validity) {
        try {
            this.validity = YearMonth.parse(validity, DateTimeFormatter.ofPattern(VALIDITY_FORMAT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, VALIDITY_FORMAT, validity));
        }
    }

    public boolean isExpired() {
        return YearMonth.now().isAfter(validity);
    }

    @JsonValue
    @Override
    public String toString() {
        return validity.format(DateTimeFormatter.ofPattern(VALIDITY_FORMAT));
    }
}
