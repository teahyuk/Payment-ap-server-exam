package com.teahyuk.payment.ap.domain.vo.card;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardInfoTest {

    private CardInfo cardInfo;
    private Uid keyUid;

    @BeforeEach
    void setting() {
        cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .build();
        keyUid = UidTest.createTestUid("123");
    }

    @Test
    void encryptAndDecryptTest() {
        String encryptedCardInfo = cardInfo.getEncryptedString(keyUid);

        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofEncryptedString(encryptedCardInfo, keyUid.getUid()));
    }

    @Test
    void decryptExpectError() {
        String encryptedCardInfo = cardInfo.getEncryptedString(keyUid);

        assertThatThrownBy(() -> CardInfo.ofEncryptedString(encryptedCardInfo, "456"))
                .isInstanceOf(CryptoException.class);
    }

}
