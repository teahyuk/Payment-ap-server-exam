package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.dto.PaymentRequest;
import com.teahyuk.payment.ap.dto.PaymentRequestTest;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.repository.PaymentRepository;
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
    private PaymentRepository paymentRepository;

    @Autowired
    private CardCompanyRepository cardCompanyRepository;

    private CardCompanyService cardCompanyService;

    private PaymentService paymentService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
        paymentService = new PaymentService(paymentRepository, cardCompanyService);
    }

    @Test
    void requestPaymentTest() {
        PaymentRequest paymentRequest = PaymentRequestTest.paymentRequest;
        Uid insertedUid = paymentService.requestPayment(paymentRequest);
        assertThat(paymentRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
        assertThat(cardCompanyRepository.findByUid(insertedUid.getUid()))
                .isNotEmpty();
    }
}
