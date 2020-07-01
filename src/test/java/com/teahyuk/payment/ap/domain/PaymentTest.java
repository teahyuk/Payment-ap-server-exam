package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTest {
    public static final Payment payment = Payment.builder()
            .cardNumber(CardNumberTest.cardNumber1)
            .validity(ValidityTest.thisMonthValidity)
            .cvc(CvcTest.cvc1)
            .installment(Installment.of(0))
            .amount(new Amount(1100))
            .vat(new Vat(5))
            .build();

    @Test
    void getStringDataTest() {
        CardCompanyDto cardCompany = payment.getCardCompanyDto();

        assertThat(cardCompany)
                .isEqualTo(CardCompanyDto.builder()
                        .uid(cardCompany.getUid())
                        .cardInfo(payment.getCardInfo())
                        .amount(payment.getAmount())
                        .vat(payment.getVat())
                        .installment(payment.getInstallment())
                        .requestType(RequestType.PAYMENT)
                        .build());
    }

    @Test
    void getPaymentStatusTest() {
        Uid uid = UidTest.createTestUid("1512312");
        PaymentStatus paymentStatus = payment.getPaymentStatus(uid);

        assertThat(paymentStatus.getUid())
                .isEqualTo(uid.getUid());

        assertThat(paymentStatus.getAmount())
                .isEqualTo(payment.getAmount().getAmount());

        assertThat(paymentStatus.getVat())
                .isEqualTo(payment.getVat().getVat());
    }

}
