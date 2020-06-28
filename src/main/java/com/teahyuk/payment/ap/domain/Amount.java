package com.teahyuk.payment.ap.domain;

import lombok.Getter;

@Getter
public class Amount {
    private final static String INVALID_FORMAT = "Create Amount error, Amount must be 100 ~ 1,000,000,000. amount=%d";
    private final static String INVALID_VAT_FORMAT = "Create Amount.getVat() error, Var must not be greater then amount. amount=%d, vat=%d";
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

    public Vat getVat(int vat) {
        if (isLess(vat)) {
            throw new IllegalArgumentException(String.format(INVALID_VAT_FORMAT, amount, vat));
        }
        return new Vat(vat);
    }

    public boolean isValidVat(Vat vat) {
        return isLess(vat.getVat());
    }

    private boolean isLess(int less) {
        return amount < less;
    }
}
