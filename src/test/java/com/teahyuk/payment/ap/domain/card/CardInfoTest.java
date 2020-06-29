package com.teahyuk.payment.ap.domain.card;

import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.domain.uid.UidTest;
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
                .isEqualTo(CardInfo.ofEncryptedString(encryptedCardInfo1, keyUid.getUid()));
        assertThat(cardInfo)
                .isEqualTo(CardInfo.ofEncryptedString(encryptedCardInfo2, keyUid.getUid()));
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

        assertThatThrownBy(() -> CardInfo.ofEncryptedString(encryptedCardInfo, keyUid.getUid()+"a"))
                .isInstanceOf(CryptoException.class);
    }

}
