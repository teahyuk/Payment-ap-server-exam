package com.teahyuk.payment.ap.domain.vo;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Amount {
    private final static int MIN = 100;
    private final static int MAX = 1000000000;
    private final static float DEFAULT_VAT_RATIO = 11.0f;
    private final static String INVALID_FORMAT = "Create Amount error, Amount must be %,d ~ %,d. amount=%,d";
    private final static String INVALID_REMAINING_FORMAT = "Get remaining Amount error, remaing Amount must be 0 or " +
            "greater equal 100. originalAmount=%s, this=%s";

    @JsonValue
    private final int amount;

    public Amount(Integer amount) {
        validationCheck(amount);
        this.amount = amount;
    }

    private void validationCheck(Integer amount) {
        if (Objects.isNull(amount) || (amount != 0 && amount < MIN) || MAX < amount) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, MIN, MAX, amount));
        }
    }

    public Vat createDefaultVat() {
        return new Vat(Math.round(amount / DEFAULT_VAT_RATIO));
    }

    public boolean isValidVat(Vat vat) {
        return vat.getVat() <= amount;
    }

    public boolean le(Amount amount) {
        return this.amount <= amount.amount;
    }

    public Amount minus(Amount amount) {
        return new Amount(this.amount - amount.amount);
    }

    @Override
    public String toString() {
        return String.format("Amount(amount=%,d)", amount);
    }
}
