package com.teahyuk.payment.ap.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardInfoTest {

    @Test
    void rawStringTest() {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(new CardNumber("0123456789012345"))
                .validity(new Validity("0125"))
                .cvc(new Cvc("623"))
                .build();

        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofRawString(cardInfo.getRawString()));
    }

}
