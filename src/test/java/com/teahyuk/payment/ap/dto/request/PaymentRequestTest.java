package com.teahyuk.payment.ap.dto.request;

import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentRequestTest {

    @Test
    void buildDefaultVatTest() throws BadRequestException {
        PaymentRequest paymentDefaultVatRequest = PaymentRequest.builder()
                .cardNumber("0123456789")
                .validity(ValidityTest.thisMonthValidity.getValidity())
                .cvc("072")
                .installment(4)
                .amount(1100)
                .build();

        assertThat(paymentDefaultVatRequest.getPayment().getVat())
                .isEqualTo(new Vat(100));
    }

}
