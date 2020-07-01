package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.PaymentTest;
import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.repository.PaymentStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentServiceTest {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private CardCompanyRepository cardCompanyRepository;

    private CardCompanyService cardCompanyService;

    private PaymentService paymentService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
        paymentService = new PaymentService(paymentStatusRepository, cardCompanyService);
    }

    @Test
    void requestPaymentTest() {
        Payment payment = PaymentTest.payment;
        Uid insertedUid = paymentService.requestPayment(payment);

        Optional<PaymentStatus> paymentStatus = paymentStatusRepository.findByUid(insertedUid.getUid());

        assertThat(paymentStatus)
                .isNotEmpty()
                .map(PaymentStatus::getAmount)
                .isEqualTo(Optional.of(payment.getAmount().getAmount()));
        assertThat(paymentStatus)
                .map(PaymentStatus::getVat)
                .isEqualTo(Optional.of(payment.getVat().getVat()));
        assertThat(cardCompanyRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
    }

    @Test
    void requestCancelNotFoundTest() {
        Cancel cancel = Cancel.builder()
                .amount(new Amount(2000))
                .vat(new Vat(30))
                .originUid(UidTest.createTestUid("notFoundOriginUid"))
                .build();

        StatusResponse<Uid> cancelResponse = paymentService.requestCancel(cancel);

        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.NOT_FOUND);
        System.out.println(cancelResponse.getErrMessage());
    }

    @Test
    void requestCancelTest() {
        //given
        Payment payment = Payment.builder()
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .installment(Installment.of(0))
                .amount(new Amount(11000))
                .vat(new Vat(100))
                .build();
        Uid insertedUid = paymentService.requestPayment(payment);

        Cancel cancel = Cancel.builder()
                .amount(new Amount(1000))
                .vat(new Vat(30))
                .originUid(insertedUid)
                .build();

        //when
        StatusResponse<Uid> cancelResponse = paymentService.requestCancel(cancel);

        //then
        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.SUCCESS);

        assertThat(cardCompanyRepository.findByUid(cancelResponse.getData().getUid()))
                .isNotEmpty();

        assertThat(paymentStatusRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty()
                .map(PaymentStatus::getAmount)
                .isEqualTo(Optional.of(10000)); // 11000 - 1000

        assertThat(paymentStatusRepository.findByUid(insertedUid.getUid()))
                .map(PaymentStatus::getVat)
                .isEqualTo(Optional.of(70)); // 100 - 30
    }

    @Test
    void requestCancelBadRequestTest() {
        //given
        Payment payment = Payment.builder()
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .installment(Installment.of(0))
                .amount(new Amount(110))
                .vat(new Vat(100))
                .build();
        Uid insertedUid = paymentService.requestPayment(payment);

        Cancel cancel = Cancel.builder()
                .amount(new Amount(1000))
                .vat(new Vat(30))
                .originUid(insertedUid)
                .build();

        //when
        StatusResponse<Uid> cancelResponse = paymentService.requestCancel(cancel);

        //then
        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.BAD_REQUEST);
        System.out.println(cancelResponse.getErrMessage());
    }
}
