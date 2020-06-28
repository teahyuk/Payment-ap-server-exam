package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Vat {
    private final static String INVALID_FORMAT = "Create Vat error, Vat must be 0 ~ 1,000,000,000. vat=%d";
    private final int vat;

    public Vat(int vat) {
        this.vat = vat;
        validationCheck();
    }

    private void validationCheck() {
        if (vat < 0 || 1000000000 < vat) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, vat));
        }
    }

    @JsonValue
    public int getVat() {
        return vat;
    }

    @Override
    public String toString() {
        return String.format("%,d", vat);
    }
}
