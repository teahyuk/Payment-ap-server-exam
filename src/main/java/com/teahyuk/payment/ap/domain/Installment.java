package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Installment {
    private final static String INVALID_FORMAT = "Create Installment error, Installment must be 0,2~12. installment=%d";
    private final int installment;

    public Installment(int installment) {
        this.installment = installment;
        validationCheck();
    }

    private void validationCheck() {
        if (installment < 0 || installment == 1 || 12 < installment) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, installment));
        }
    }

    @JsonValue
    public int getInstallment() {
        return installment;
    }

    @Override
    public String toString() {
        return String.format("%2d", installment);
    }
}
