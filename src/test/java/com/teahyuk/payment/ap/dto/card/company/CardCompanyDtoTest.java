package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.*;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.domain.uid.UidTest;
import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardCompanyDtoTest {
    @Test
    void paymentValidTest() {
        assertThat(buildStringDataRequest(RequestType.PAYMENT, Installment.of(0), null).isValid())
                .isTrue();
    }

    @Test
    void paymentInvalidTest() {
        assertThat(buildStringDataRequest(RequestType.PAYMENT, null, UidTest.createTestUid("0123456")).isValid())
                .isFalse();
    }

    @Test
    void cancelValidTest() {
        assertThat(buildStringDataRequest(RequestType.CANCEL, null, UidTest.createTestUid("0123456")).isValid())
                .isTrue();
    }

    @Test
    void cancelInvalidTest() {
        assertThat(buildStringDataRequest(RequestType.CANCEL, Installment.of(0), null).isValid())
                .isFalse();

        assertThat(buildStringDataRequest(RequestType.CANCEL, null, null).isValid())
                .isFalse();
    }

    private CardCompanyDto buildStringDataRequest(RequestType requestType, Installment installment,
                                                  Uid originUid) {
        return CardCompanyDto.builder()
                .requestType(requestType)
                .uid(UidTest.createTestUid("01234567890123456789"))
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(new Cvc("012"))
                        .build())
                .amount(new Amount(20000))
                .vat(new Vat(1818))
                .installment(installment)
                .originUid(originUid)
                .build();
    }

    @Test
    void getSerializedDataTest() throws CryptoException {
        String uid = "01234567890123456789";
        String cardNumber = "0123456789123";
        String validity = "0520";
        String cvc = "012";
        int amount = 20000;
        int vat = 1818;
        int installment = 12;
        String originUid = "98765432109876543210";

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(new CardNumber(cardNumber))
                .validity(new Validity(validity))
                .cvc(new Cvc(cvc))
                .build();

        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .uid(UidTest.createTestUid(uid))
                .cardInfo(cardInfo)
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .installment(Installment.of(installment))
                .originUid(UidTest.createTestUid(originUid))
                .build();

        String serializedString = cardCompanyDto.getSerializedString();
        assertThat(serializedString.substring(0, serializedString.length() - 347)) //암호화 되지 않은 부분 확인
                .isEqualTo(getSerializedStringWithoutEncrypted(RequestType.PAYMENT.name(), uid, cardNumber, validity, cvc, amount, vat, installment, originUid));
        String encryptedCardInfo = serializedString.substring(103, 403).trim();
        assertThat(CardInfo.ofEncryptedString(encryptedCardInfo, UidTest.createTestUid(uid)))
                .isEqualTo(cardInfo);
    }

    private String getSerializedStringWithoutEncrypted(String requestType, String uid, String cardNumber, String validity, String cvc, int amount, int vat, int installment, String originUid) {
        return String.format(
                " 446" +
                        "%-10s" +
                        "%s" +
                        "%-20s" +
                        "%02d" +
                        "%s" +
                        "%s" +
                        "%10s" +
                        "%010d" +
                        "%20s",
                requestType,
                uid,
                cardNumber,
                installment,
                validity,
                cvc,
                amount,
                vat,
                originUid);
    }

    @Test
    void fromSerializedStringTest() throws CryptoException {
        String uid = "01234567890123456789";
        String cardNumber = "0123456789123";
        String validity = "0520";
        String cvc = "012";
        int amount = 20000;
        int vat = 1818;
        int installment = 12;
        String originUid = "98765432109876543210";

        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(new CardNumber(cardNumber))
                .validity(new Validity(validity))
                .cvc(new Cvc(cvc))
                .build();

        String serializedString = getSerializedStringWithoutEncrypted(RequestType.CANCEL.name(), uid, cardNumber, validity, cvc, amount, vat, installment, originUid);
        serializedString = String.format("%s%-347s", serializedString, cardInfo.getEncryptedString(UidTest.createTestUid(uid)));

        CardCompanyDto expectCardCompanyDto = CardCompanyDto.builder()
                .requestType(RequestType.CANCEL)
                .uid(UidTest.createTestUid(uid))
                .cardInfo(cardInfo)
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .installment(Installment.of(installment))
                .originUid(UidTest.createTestUid(originUid))
                .build();

        assertThat(CardCompanyDto.fromSerialized(serializedString))
                .isEqualTo(expectCardCompanyDto);
    }
}
