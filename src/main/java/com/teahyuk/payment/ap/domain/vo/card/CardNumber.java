package com.teahyuk.payment.ap.domain.vo.card;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class CardNumber {
    private final static String INVALID_FORMAT = "Create card number error, card number must be between 10 to 16 digits. cardNumbers=%s";

    @JsonValue
    private final String cardNumber;

    public CardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        validationCheck();
    }

    private void validationCheck() {
        int digits = cardNumber == null ? 0 : cardNumber.length();
        if (digits < 10 || 16 < digits) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, cardNumber));
        }
    }
}
