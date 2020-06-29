package com.teahyuk.payment.ap.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardInfoTest {

    @Test
    void rawStringTest() {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .build();

        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofRawString(cardInfo.getRawString()));
    }

}
