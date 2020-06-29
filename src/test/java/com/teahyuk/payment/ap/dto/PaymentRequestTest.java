package com.teahyuk.payment.ap.dto;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.card.CvcTest;
import com.teahyuk.payment.ap.domain.card.ValidityTest;
import com.teahyuk.payment.ap.domain.entity.Payment;
import com.teahyuk.payment.ap.domain.entity.StringData;
import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRequestTest {

    @Test
    void buildDefaultVatTest() {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .installment(Installment.of(0))
                .amount(new Amount(1100))
                .build();

        assertThat(paymentRequest.getVat())
                .isEqualTo(new Vat(100));
    }

    @Test
    void getStringDataTest() throws CryptoException {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .validity(ValidityTest.thisMonthValidity)
                .cvc(CvcTest.cvc1)
                .installment(Installment.of(0))
                .amount(new Amount(20000))
                .build();
        StringData stringData = paymentRequest.getStringData();

        assertThat(stringData.getUid())
                .isEqualTo(stringData.getString().substring(14,34));
    }

}
