package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Cvc {
    private final static String INVALID_FORMAT = "Create Cvc error, cvc must be 3 digits number, cvc=%s";
    private final String cvc;

    public Cvc(String cvc) {
        this.cvc = cvc;
        checkValidation();
    }

    private void checkValidation() {
        if (!isInt() || isNot3Digits()) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, cvc));
        }
    }

    private boolean isNot3Digits() {
        return cvc.length() != 3;
    }

    private boolean isInt() {
        try {
            Integer.parseInt(cvc);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @JsonValue
    public String getCvc() {
        return cvc;
    }
}
