package com.teahyuk.payment.ap.domain;

import lombok.Getter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
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

}
