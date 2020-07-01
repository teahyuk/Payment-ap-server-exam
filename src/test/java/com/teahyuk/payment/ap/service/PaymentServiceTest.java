package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.PaymentTest;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.repository.PaymentStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentServiceTest {

    @Autowired
    private PaymentStateRepository paymentStateRepository;

    @Autowired
    private CardCompanyRepository cardCompanyRepository;

    private CardCompanyService cardCompanyService;

    private PaymentService paymentService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
        paymentService = new PaymentService(paymentStateRepository, cardCompanyService);
    }

    @Test
    void requestPaymentTest() {
        Payment payment = PaymentTest.PAYMENT;
        Uid insertedUid = paymentService.requestPayment(payment);
        assertThat(paymentStateRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
        assertThat(cardCompanyRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
    }
}
