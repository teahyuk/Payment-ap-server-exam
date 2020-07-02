package com.teahyuk.payment.ap.domain.vo.card;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@ToString
public class Cvc {
    private final static int LENGTH = 3;
    private final static String INVALID_FORMAT = "Create Cvc error, cvc must be %d digits number, cvc=%s";
    private final static Pattern numberPattern = Pattern.compile("\\d+?");

    @JsonValue
    private final String cvc;

    public Cvc(String cvc) {
        this.cvc = cvc;
        checkValidation();
    }

    private void checkValidation() {
        if (isInvalidNumber() || isNot3Digits()) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, LENGTH, cvc));
        }
    }

    private boolean isNot3Digits() {
        return cvc.length() != LENGTH;
    }

    private boolean isInvalidNumber() {
        if (cvc == null) {
            return true;
        }
        return !numberPattern.matcher(cvc).matches();
    }
}
