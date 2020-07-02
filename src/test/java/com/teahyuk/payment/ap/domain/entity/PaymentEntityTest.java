package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentEntityTest {
    @Test
    void getCardInfoTest() {
        //given
        CardInfo expectCardInfo = CardInfo.builder()
                .cardNumber(new CardNumber("0123456789"))
                .validity(new Validity("0521"))
                .cvc(new Cvc("331"))
                .build();
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .cardInfo(expectCardInfo)
                .uid(UidTest.createTestUid("_testUid_"))
                .build();

        //when
        assertThat(paymentEntity.getCardInfo())
                .isEqualTo(expectCardInfo);
    }
}
