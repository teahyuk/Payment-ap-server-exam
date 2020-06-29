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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringDataRequestTest {
    private Uid uid;
    private CardInfo cardInfo;
    private StringDataRequest paymentRequest;
    private StringDataRequest cancelRequest;

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
        cancelRequest = StringDataRequest.builder()
                .requestType(RequestType.CANCEL)
                .uid(UidTest.createTestUid("98765432109876543210"))
                .amount(new Amount(10))
                .vat(new Vat(1))
                .originUid(uid.getUid())
                .encryptedCardInfo(paymentRequest.getEncryptedCardInfo())
                .build();
    }

    @Test
    void buildFailTest() {
        assertThatThrownBy(() -> buildStringDataRequest(RequestType.PAYMENT, null, Installment.of(0), uid.getUid(),
                paymentRequest.getEncryptedCardInfo()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> buildStringDataRequest(RequestType.PAYMENT, cardInfo, null, uid.getUid(),
                paymentRequest.getEncryptedCardInfo()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> buildStringDataRequest(RequestType.CANCEL, cardInfo, Installment.of(0), null,
                paymentRequest.getEncryptedCardInfo()))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> buildStringDataRequest(RequestType.CANCEL, cardInfo, Installment.of(0), uid.getUid(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void buildStringDataRequest(RequestType requestType, CardInfo cardInfo, Installment installment,
                                        String originUid, String encryptedCardInfo) throws CryptoException {
        StringDataRequest.builder()
                .requestType(requestType)
                .uid(uid)
                .cardInfo(cardInfo)
                .amount(new Amount(100))
                .vat(new Vat(9))
                .installment(installment)
                .originUid(originUid)
                .encryptedCardInfo(encryptedCardInfo)
                .build();
    }

    @Test
    void getStringDataTest() {
        assertThat(paymentRequest.getStringData())
                .isEqualTo(String.format(
                        " 446" +
                                "PAYMENT   " +
                                "01234567890123456789" +
                                "0123456789          " +
                                "03" +
                                "%s" +
                                "012" +
                                "     20000" +
                                "0000001818" +
                                "                    " +
                                "%-300s" +
                                "                                               ",
                        ValidityTest.thisMonthValidity.getValidity(),
                        paymentRequest.getEncryptedCardInfo()));
    }

    @Test
    void getUidTest() {
        assertThat(paymentRequest.getUid())
                .isEqualTo(uid);
    }

    @Test
    void getEncryptedCardInfoTest() throws CryptoException {
        assertThat(CardInfo.ofEncryptedString(paymentRequest.getEncryptedCardInfo(), uid.getUid()))
                .isEqualTo(cardInfo);
    }

}
