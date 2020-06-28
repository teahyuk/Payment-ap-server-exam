package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.CardInfo;
import com.teahyuk.payment.ap.domain.CardNumber;
import com.teahyuk.payment.ap.domain.Cvc;
import com.teahyuk.payment.ap.domain.Validity;
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
                .cardNumber(new CardNumber("000000000012345"))
                .validity(new Validity("0125"))
                .cvc(new Cvc("326"))
                .build();
        String uid = "testtesttesttesttest";
        String encrypted = cardInfoCryptoProvider.encrypt(cardInfo, uid);
        assertThat(encrypted)
                .isNotEqualTo(cardInfo.getRawString());
        assertThat(cardInfoCryptoProvider.decrypt(encrypted,uid))
                .isEqualTo(cardInfo);
    }

}
