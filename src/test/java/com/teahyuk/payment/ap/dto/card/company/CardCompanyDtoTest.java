package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.*;
import com.teahyuk.payment.ap.domain.entity.CardCompany;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class CardCompanyDtoTest {
    @Test
    void getSerializedDataTest() {
        //given
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
        CardCompanyDto cardCompanyDto = buildCardCompanyDto(RequestType.PAYMENT, uid, amount, vat, installment, UidTest.createTestUid(originUid), cardInfo);

        //when
        String serializedString = cardCompanyDto.getSerializedString();

        //then
        assertThat(serializedString.substring(0, serializedString.length() - 347)) //암호화 되지 않은 부분 확인
                .isEqualTo(getSerializedStringWithoutEncrypted(RequestType.PAYMENT.name(), uid, cardNumber, validity, cvc, amount, vat, installment, originUid));
        String encryptedCardInfo = serializedString.substring(103, 403).trim();
        assertThat(CardInfo.ofEncryptedString(encryptedCardInfo, uid))
                .isEqualTo(cardInfo);
    }

    private CardCompanyDto buildCardCompanyDto(RequestType requestType, String uid, int amount, int vat, int installment, Uid originUid, CardInfo cardInfo) {
        return CardCompanyDto.builder()
                .requestType(requestType)
                .uid(UidTest.createTestUid(uid))
                .cardInfo(cardInfo)
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .installment(Installment.of(installment))
                .originUid(originUid)
                .build();
    }

    private String getSerializedStringWithoutEncrypted(String requestType, String uid, String cardNumber, String validity, String cvc, int amount, int vat, int installment, String originUid) {
        return String.format(
                " 446" + "%-10s" + "%s" + "%-20s" + "%02d" + "%s" + "%s" + "%10s" + "%010d" + "%20s",
                Objects.toString(requestType, ""),
                Objects.toString(uid, ""),
                Objects.toString(cardNumber, ""),
                installment,
                Objects.toString(validity, ""),
                Objects.toString(cvc, ""),
                amount,
                vat,
                Objects.toString(originUid, ""));
    }

    @Test
    void fromSerializedStringTest() {
        //given
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

        //when
        CardCompanyDto deserializeDto = CardCompanyDto.fromSerialized(serializedString);

        //then
        CardCompanyDto expectCardCompanyDto = buildCardCompanyDto(RequestType.CANCEL, uid, amount, vat, installment, UidTest.createTestUid(originUid), cardInfo);
        assertThat(deserializeDto)
                .isEqualTo(expectCardCompanyDto);
    }

    @Test
    void fromSerializedStringOriginUidNullTest() {
        //given
        String uid = "01234567890123456789";
        String cardNumber = "0123456789123";
        String validity = "0520";
        String cvc = "012";
        int amount = 20000;
        int vat = 1818;
        int installment = 12;
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(new CardNumber(cardNumber))
                .validity(new Validity(validity))
                .cvc(new Cvc(cvc))
                .build();

        String serializedString = getSerializedStringWithoutEncrypted(RequestType.CANCEL.name(), uid, cardNumber, validity, cvc, amount, vat, installment, null);
        serializedString = String.format("%s%-347s", serializedString, cardInfo.getEncryptedString(UidTest.createTestUid(uid)));

        //when
        CardCompanyDto deserializeDto = CardCompanyDto.fromSerialized(serializedString);

        //then
        CardCompanyDto expectCardCompanyDto = buildCardCompanyDto(RequestType.CANCEL, uid, amount, vat, installment, null, cardInfo);
        assertThat(deserializeDto)
                .isEqualTo(expectCardCompanyDto);
    }

    @Test
    void refreshUidTest() {
        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(11000))
                .installment(Installment.of(5))
                .requestType(RequestType.PAYMENT)
                .vat(new Vat(1000))
                .build();

        Uid oldUid = cardCompanyDto.getUid();
        Uid refUid = cardCompanyDto.refreshUid();
        Uid newUid = cardCompanyDto.getUid();

        assertThat(oldUid)
                .isNotEqualTo(refUid)
                .isNotEqualTo(newUid);
        assertThat(refUid)
                .isEqualTo(newUid);
    }

    @Test
    void toEntityTest() {
        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(11000))
                .installment(Installment.of(5))
                .requestType(RequestType.PAYMENT)
                .vat(new Vat(1000))
                .uid(UidTest.createTestUid("4252"))
                .build();

        CardCompany cardCompany = cardCompanyDto.toEntity();

        assertThat(cardCompany.getUid())
                .isEqualTo(UidTest.createTestUid("4252").getUid());
        assertThat(CardCompanyDto.fromSerialized(cardCompany.getString()))
                .isEqualTo(cardCompanyDto);
    }

}
