package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.*;
import com.teahyuk.payment.ap.util.AES256Crypto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {CardInfoCryptoProvider.class, AES256Crypto.class})
class CardInfoCryptoProviderTest {

    @Autowired
    private CardInfoCryptoProvider cardInfoCryptoProvider;

    @Test
    void testEncryptAndDecrypt() throws Exception {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .build();
        String uid = "testtesttesttesttest";
        String encrypted = cardInfoCryptoProvider.encrypt(cardInfo, uid);
        assertThat(encrypted)
                .isNotEqualTo(cardInfo.getRawString());
        assertThat(cardInfoCryptoProvider.decrypt(encrypted,uid))
                .isEqualTo(cardInfo);
    }

}
