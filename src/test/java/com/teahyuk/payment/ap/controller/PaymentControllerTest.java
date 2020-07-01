package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.dto.PaymentRequest;
import com.teahyuk.payment.ap.dto.PaymentRequestTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void postPaymentTest() throws Exception {
        PaymentRequest paymentRequest = PaymentRequestTest.paymentRequest;
        /*
         {
             "cardNumber": "0123456789",
             "validity": "0720",
             "cvc": "000",
             "installment": 0,
             "amount": 1100,
             "vat": 100
         }
         */
        mvc.perform(
                post("/v1/payment")
                        .content(new ObjectMapper().writeValueAsString(paymentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("uid").isString());
    }

}
