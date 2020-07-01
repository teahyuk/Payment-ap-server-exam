package com.teahyuk.payment.ap.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Vat {
    private final static int MIN = 0;
    private final static int MAX = 1000000000;
    private final static String INVALID_FORMAT = "Create Vat error, Vat must be %,d ~ %,d. vat=%,d";
    private final static String INVALID_REMAINING_FORMAT = "Get remaining Vat error, Vat must be positive " +
            "originalVat=%s, this=%s";

    @JsonValue
    private final int vat;

    public Vat(int vat) {
        this.vat = vat;
        validationCheck();
    }

    private void validationCheck() {
        if (vat < MIN || MAX < vat) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, MIN, MAX, vat));
        }
    }

    public boolean le(int vat) {
        return this.vat <= vat;
    }

    public int getRemaining(int originVat) {
        int result = originVat - this.vat;
        if (result < 0) {
            throw new ArithmeticException(String.format(INVALID_REMAINING_FORMAT, originVat, vat));
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("Vat(vat=%,d)", vat);
    }
}
