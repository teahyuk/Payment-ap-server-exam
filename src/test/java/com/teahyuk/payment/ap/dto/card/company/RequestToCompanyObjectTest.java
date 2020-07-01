package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

class RequestToCompanyObjectTest {

    @Test
    void getSerializedDataTest() {
        //given
        RequestType requestType = RequestType.CANCEL;
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
        RequestToCompanyObject serializer = RequestToCompanyObject.builder()
                .requestType(requestType)
                .cardInfo(cardInfo)
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .installment(Installment.of(installment))
                .originUid(new Uid(originUid))
                .build();

        //when
        String serializedString = serializer.serialize(UidTest.createTestUid(uid));

        //then
        assertThat(serializedString.substring(0, serializedString.length() - 347)) //암호화 되지 않은 부분 확인
                .isEqualTo(getSerializedStringWithoutEncrypted(requestType.name(), uid, cardNumber, validity, cvc, amount, vat, installment, originUid));
        String encryptedCardInfo = serializedString.substring(103, 403).trim();
        assertThat(CardInfo.ofEncryptedString(encryptedCardInfo, uid))
                .isEqualTo(cardInfo);
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
    void expectCardCompanySerializeExceptionTest() {
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(new CardNumber("0123456789"))
                .validity(new Validity("0123"))
                .cvc(new Cvc("356"))
                .build();
        RequestToCompanyObject serializerPayment = RequestToCompanyObject.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(cardInfo)
                .amount(new Amount(2000))
                .vat(new Vat(5))
                .installment(Installment.of(3))
                .originUid(UidTest.createTestUid("origin"))
                .build();

        RequestToCompanyObject serializerCancel = RequestToCompanyObject.builder()
                .requestType(RequestType.CANCEL)
                .cardInfo(cardInfo)
                .amount(new Amount(2000))
                .vat(new Vat(5))
                .installment(Installment.of(3))
                .build();

        assertThatThrownBy(() -> serializerPayment.serialize(UidTest.createTestUid("uid")))
                .isInstanceOf(CardCompanySerializeException.class);

        assertThatThrownBy(() -> serializerCancel.serialize(UidTest.createTestUid("uid")))
                .isInstanceOf(CardCompanySerializeException.class);
    }
}
