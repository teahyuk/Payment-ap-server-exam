package com.teahyuk.payment.ap.dto;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.card.CvcTest;
import com.teahyuk.payment.ap.domain.card.ValidityTest;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.dto.card.company.RequestType;
import com.teahyuk.payment.ap.util.CryptoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentRequestTest {
    public static final PaymentRequest paymentRequest = PaymentRequest.builder()
            .cardNumber(CardNumberTest.cardNumber1)
            .validity(ValidityTest.thisMonthValidity)
            .cvc(CvcTest.cvc1)
            .installment(Installment.of(0))
            .amount(new Amount(1100))
            .build();

    @Test
    void buildDefaultVatTest() {
        assertThat(paymentRequest.getVat())
                .isEqualTo(new Vat(100));
    }

    @Test
    void getStringDataTest() {
        CardCompanyDto cardCompany = paymentRequest.getCardCompanyDto();

        assertThat(cardCompany)
                .isEqualTo(CardCompanyDto.builder()
                        .uid(cardCompany.getUid())
                        .cardInfo(CardInfo.builder()
                                .cardNumber(paymentRequest.getCardNumber())
                                .validity(paymentRequest.getValidity())
                                .cvc(paymentRequest.getCvc())
                                .build())
                        .amount(paymentRequest.getAmount())
                        .vat(paymentRequest.getVat())
                        .installment(paymentRequest.getInstallment())
                        .requestType(RequestType.PAYMENT)
                        .build());
    }

}
