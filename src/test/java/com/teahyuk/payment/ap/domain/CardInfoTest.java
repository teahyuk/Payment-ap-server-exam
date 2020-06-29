package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardInfoTest {
    @Test
    void encryptAndDecryptTest() throws CryptoException {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .build();
        Uid keyUid = UidTest.createTestUid("123");

        String encryptedCardInfo1 = cardInfo.getEncryptedString(keyUid);
        String encryptedCardInfo2 = cardInfo.getEncryptedString(keyUid);

        assertThat(encryptedCardInfo1)
                .isNotEqualTo(encryptedCardInfo2);

        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofEncryptedString(encryptedCardInfo1, keyUid));
        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofEncryptedString(encryptedCardInfo2, keyUid));
    }

    @Test
    void decryptExpectError() throws CryptoException {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .build();
        Uid keyUid = UidTest.createTestUid("123");

        String encryptedCardInfo = cardInfo.getEncryptedString(keyUid);

        assertThatThrownBy(() -> CardInfo.ofEncryptedString(encryptedCardInfo, UidTest.createTestUid("1")))
                .isInstanceOf(CryptoException.class);
    }

}
