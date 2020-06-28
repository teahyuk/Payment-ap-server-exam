package com.teahyuk.payment.ap.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Vat {
    private final static String INVALID_FORMAT = "Create Vat error, Vat must be 0 ~ 9,999,999,999. vat=%d";
    private final long vat;

    Vat(long vat) {
        this.vat = vat;
        validationCheck();
    }

    private void validationCheck() {
        if (vat < 0 || 9999999999L < vat) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, vat));
        }
    }

}
