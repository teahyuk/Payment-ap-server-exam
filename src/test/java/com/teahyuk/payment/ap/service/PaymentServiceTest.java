package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.PaymentTest;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.repository.PaymentRepository;
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
    private CardCompanyRepository cardCompanyRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private CardCompanyService cardCompanyService;

    private PaymentService paymentService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
        paymentService = new PaymentService(cardCompanyService, paymentRepository);
    }

    @Test
    void requestPaymentTest() {
        Payment payment = PaymentTest.payment;
        Uid insertedUid = paymentService.requestPayment(payment);

        Optional<PaymentEntity> paymentStatus = paymentRepository.findByUid(insertedUid.getUid());

        assertThat(paymentStatus)
                .isNotEmpty()
                .map(PaymentEntity::getAmount)
                .isEqualTo(Optional.of(payment.getAmount().getAmount()));
        assertThat(paymentStatus)
                .map(PaymentEntity::getVat)
                .isEqualTo(Optional.of(payment.getVat().getVat()));
        assertThat(cardCompanyRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
        assertThat(paymentRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
    }
}
