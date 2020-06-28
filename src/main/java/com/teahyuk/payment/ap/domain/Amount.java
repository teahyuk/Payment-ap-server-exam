package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Amount {
    public final static Amount MAX_VALUE = new Amount(1000000000);

    private final static String INVALID_FORMAT = "Create Amount error, Amount must be 100 ~ 1,000,000,000. amount=%d";

    private final int amount;

    public Amount(int amount) {
        this.amount = amount;
        validationCheck();
    }

    private void validationCheck() {
        if (amount < 100 || 1000000000 < amount) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, amount));
        }
    }

    public Vat getVat() {
        return new Vat(Math.round(amount / 11.0f));
    }

    public boolean isValidVat(Vat vat) {
        return vat.getVat() <= amount;
    }

    @JsonValue
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%,d", amount);
    }
}
