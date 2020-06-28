package com.teahyuk.payment.ap.domain;

import lombok.Getter;

@Getter
public class Amount {
    private final static String INVALID_FORMAT = "Create Amount error, Amount must be 100~9,999,999,999. amount=%d";
    private final static String INVALID_VAT_FORMAT = "Create Amount.getVat() error, Var must be less then amount. amount=%d, vat=%d";
    private final long amount;

    public Amount(long amount) {
        this.amount = amount;
        validationCheck();
    }

    private void validationCheck() {
        if (amount < 100 || 9999999999L < amount) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, amount));
        }
    }

    public Vat getVat() {
        return new Vat(Math.round(amount / 11.0d));
    }

    public Vat getVat(long vat) {
        if (isLess(vat)) {
            throw new IllegalArgumentException(String.format(INVALID_VAT_FORMAT, amount, vat));
        }
        return new Vat(vat);
    }

    public boolean isValidVat(Vat vat) {
        return isLess(vat.getVat());
    }

    private boolean isLess(long less) {
        return amount < less;
    }
}
