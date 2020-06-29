package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.card.Cvc;
import com.teahyuk.payment.ap.domain.card.ValidityTest;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.domain.uid.UidTest;
import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StringDataRequestTest {
    private Uid uid;
    private CardInfo cardInfo;
    private StringDataRequest paymentRequest;

    @BeforeEach
    void setting() throws CryptoException {
        uid = UidTest.createTestUid("01234567890123456789");
        cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(new Cvc("012"))
                .build();
        paymentRequest = StringDataRequest.builder()
                .requestType(RequestType.PAYMENT)
                .uid(uid)
                .cardInfo(cardInfo)
                .amount(new Amount(20000))
                .vat(new Vat(1818))
                .installment(Installment.of(0))
                .build();
    }

    @Test
    void buildCancelTest() {
        assertThatCode(() -> StringDataRequest.builder()
                .requestType(RequestType.CANCEL)
                .uid(UidTest.createTestUid("98765432109876543210"))
                .cardInfo(cardInfo)
                .amount(new Amount(100))
                .vat(new Vat(1))
                .originUid(uid.getUid())
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void buildFailTest() {
        assertThatThrownBy(() -> buildStringDataRequest(RequestType.PAYMENT, null, uid.getUid()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> buildStringDataRequest(RequestType.CANCEL, Installment.of(0), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void buildStringDataRequest(RequestType requestType, Installment installment,
                                        String originUid) throws CryptoException {
        StringDataRequest.builder()
                .requestType(requestType)
                .uid(uid)
                .cardInfo(cardInfo)
                .amount(new Amount(100))
                .vat(new Vat(9))
                .installment(installment)
                .originUid(originUid)
                .build();
    }

    @Test
    void getStringDataTest() throws CryptoException {
        String serializedString = paymentRequest.getStringData();
        assertThat(serializedString.substring(0, serializedString.length() - 347))
                .isEqualTo(String.format(
                        " 446" +
                                "PAYMENT   " +
                                "01234567890123456789" +
                                "0123456789          " +
                                "00" +
                                "%s" +
                                "012" +
                                "     20000" +
                                "0000001818" +
                                "                    ",
                        ValidityTest.thisMonthValidity.getValidity()));
        String encryptedCardInfo = serializedString.substring(103, 300).trim();
        assertThat(CardInfo.ofEncryptedString(encryptedCardInfo, uid.getUid()))
                .isEqualTo(cardInfo);
    }

    @Test
    void getUidTest() {
        assertThat(paymentRequest.getUid())
                .isEqualTo(uid);
    }

}
