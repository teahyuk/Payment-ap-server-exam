package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class CardNumber {
    private final static String INVALID_FORMAT = "Create card number error, card number must be between 10 to 16 digits. cardNumbers=%s";
    private final String cardNumber;

    public CardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        validationCheck();
    }

    private void validationCheck() {
        int digits = cardNumber.length();
        if (digits < 10 || 16 < digits) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, cardNumber));
        }
    }

    @JsonValue
    public String getCardNumber() {
        return cardNumber;
    }
}
