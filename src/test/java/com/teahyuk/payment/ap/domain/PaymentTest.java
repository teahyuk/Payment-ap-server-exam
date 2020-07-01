package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {
    public static final Payment PAYMENT = Payment.builder()
            .cardNumber(CardNumberTest.cardNumber1)
            .validity(ValidityTest.thisMonthValidity)
            .cvc(CvcTest.cvc1)
            .installment(Installment.of(0))
            .amount(new Amount(1100))
            .build();

    @Test
    void buildDefaultVatTest() {
        assertThat(PAYMENT.getVat())
                .isEqualTo(new Vat(100));
    }

    @Test
    void getStringDataTest() {
        CardCompanyDto cardCompany = PAYMENT.getCardCompanyDto();

        assertThat(cardCompany)
                .isEqualTo(CardCompanyDto.builder()
                        .uid(cardCompany.getUid())
                        .cardInfo(CardInfo.builder()
                                .cardNumber(PAYMENT.getCardNumber())
                                .validity(PAYMENT.getValidity())
                                .cvc(PAYMENT.getCvc())
                                .build())
                        .amount(PAYMENT.getAmount())
                        .vat(PAYMENT.getVat())
                        .installment(PAYMENT.getInstallment())
                        .requestType(RequestType.PAYMENT)
                        .build());
    }

}
