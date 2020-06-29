package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Amount {
    private final static int MIN = 100;
    private final static int MAX = 1000000000;
    private final static float DEFAULT_VAT_RATIO = 11.0f;
    private final static String INVALID_FORMAT = "Create Amount error, Amount must be %,d ~ %,d. amount=%,d";

    @JsonValue
    private final int amount;

    public Amount(int amount) {
        this.amount = amount;
        validationCheck();
    }

    private void validationCheck() {
        if (amount < MIN || MAX < amount) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, MIN, MAX, amount));
        }
    }

    public Vat createDefaultVat() {
        return new Vat(Math.round(amount / DEFAULT_VAT_RATIO));
    }

    public boolean isValidVat(Vat vat) {
        return vat.getVat() <= amount;
    }

    @Override
    public String toString() {
        return String.format("Amount(amount=%,d)", amount);
    }
}
