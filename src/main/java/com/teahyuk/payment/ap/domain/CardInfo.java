package com.teahyuk.payment.ap.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CardInfo {
    private static final String MEMBER_SEPARATOR = "\\|";

    private final CardNumber cardNumber;
    private final Validity validity;
    private final Cvc cvc;

    public String getRawString() {
        return String.join(MEMBER_SEPARATOR, cardNumber.getCardNumber(), validity.getValidity(), cvc.getCvc());
    }

    public static CardInfo ofRawString(String rawString){
        String[] rawMembers = rawString.split(MEMBER_SEPARATOR);
        return CardInfo.builder()
                .cardNumber(new CardNumber(rawMembers[0].substring(0,rawMembers[0].length()-1)))
                .validity(new Validity(rawMembers[1].substring(0,rawMembers[1].length()-1)))
                .cvc(new Cvc(rawMembers[2]))
                .build();
    }
}
