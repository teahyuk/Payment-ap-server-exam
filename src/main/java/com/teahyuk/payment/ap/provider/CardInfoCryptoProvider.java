package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.CardInfo;
import com.teahyuk.payment.ap.util.CryptoException;
import com.teahyuk.payment.ap.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardInfoCryptoProvider {

    @Autowired
    CryptoUtil cryptoUtil;

    public String encrypt(CardInfo cardInfo, String uid) throws CryptoException {
        return cryptoUtil.encrypt(cardInfo.getRawString(), uid);
    }

    public CardInfo decrypt(String encryptedCardInfo, String uid) throws CryptoException {
        return CardInfo.ofRawString(cryptoUtil.decrypt(encryptedCardInfo, uid));
    }
}
