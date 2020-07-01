package com.teahyuk.payment.ap.domain.vo.card;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.util.AES256Crypto;
import com.teahyuk.payment.ap.util.CryptoException;
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

    public String getEncryptedString(Uid uid) throws CryptoException {
        return AES256Crypto.encrypt(getRawString(), uid.getUid());
    }

    private String getRawString() {
        return String.join(MEMBER_SEPARATOR, cardNumber.getCardNumber(), validity.getValidity(), cvc.getCvc());
    }

    public static CardInfo ofEncryptedString(String encryptedString, Uid uid) throws CryptoException {
        return ofRawString(AES256Crypto.decrypt(encryptedString, uid.getUid()));
    }

    private static CardInfo ofRawString(String rawString) {
        String[] rawMembers = rawString.split(MEMBER_SEPARATOR);
        return CardInfo.builder()
                .cardNumber(new CardNumber(rawMembers[0].substring(0, rawMembers[0].length() - 1)))
                .validity(new Validity(rawMembers[1].substring(0, rawMembers[1].length() - 1)))
                .cvc(new Cvc(rawMembers[2]))
                .build();
    }
}
