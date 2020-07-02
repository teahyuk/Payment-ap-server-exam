package com.teahyuk.payment.ap.domain.vo.card;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
@ToString
public class CardNumber {
    private final static int MIN_LENGTH = 10;
    private final static int MAX_LENGTH = 16;
    private final static String INVALID_FORMAT = "Create card number error, card number must be between %d to %d " +
            "numeric digits. cardNumbers=%s";
    private final static Pattern numberPattern = Pattern.compile("\\d+?");


    @JsonValue
    private final String cardNumber;

    public CardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        validationCheck();
    }

    private void validationCheck() {
        int digits = cardNumber == null ? 0 : cardNumber.length();
        if (isInvalidNumber(cardNumber) || isInvalidLength(digits)) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, MIN_LENGTH, MAX_LENGTH, cardNumber));
        }
    }

    private boolean isInvalidLength(int digits) {
        return digits < MIN_LENGTH || MAX_LENGTH < digits;
    }

    private boolean isInvalidNumber(String cardNumber) {
        if (cardNumber == null) {
            return true;
        }
        return !numberPattern.matcher(cardNumber).matches();
    }
}
