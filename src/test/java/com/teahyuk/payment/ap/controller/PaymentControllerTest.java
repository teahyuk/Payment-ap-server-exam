package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.card.CvcTest;
import com.teahyuk.payment.ap.domain.card.ValidityTest;
import com.teahyuk.payment.ap.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testPayment() throws Exception {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .amount(new Amount(20000))
                .cvc(CvcTest.cvc1)
                .validity(ValidityTest.thisMonthValidity)
                .installment(Installment.of(12))
                .vat(new Vat(200000))
                .build();
        mvc.perform(
                post("/v1/payment")
                        .content(new ObjectMapper().writeValueAsString(paymentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(paymentRequest)));
    }

}
