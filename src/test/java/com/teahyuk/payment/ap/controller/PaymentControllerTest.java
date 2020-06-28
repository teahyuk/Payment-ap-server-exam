package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.*;
import com.teahyuk.payment.ap.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@WebFluxTest(PaymentController.class)
@Slf4j
class PaymentControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testPayment() {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(new CardNumber("0123456789"))
                .amount(new Amount(20000))
                .cvc(new Cvc("252"))
                .validity(new Validity("0124"))
                .installment(new Installment(12))
                .vat(new Vat(200000))
                .build();
        this.webTestClient.post().uri("/v1/payment").accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(paymentRequest))
                .exchange()
                .expectStatus().isOk()
                .expectBody(PaymentRequest.class)
                .isEqualTo(paymentRequest);
    }

}
