package com.teahyuk.payment.ap.dto.card.company;

public class CardCompanySerializeException extends RuntimeException {

    public CardCompanySerializeException(String message, Object... args) {
        super(String.format(message, args));
    }
}
