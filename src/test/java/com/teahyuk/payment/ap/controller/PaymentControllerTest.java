package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

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

    private static Stream<Arguments> provideSuccessDto() { // argument source method
        return Stream.of(
                Arguments.of("0123456789", "0720", "032", 4, 20000, 100),
                Arguments.of("0123456789", "0720", "032", 4, 20000, null));
    }

    @ParameterizedTest
    @MethodSource("provideSuccessDto")
    void postPaymentTest(String cardNumber, String validity, String cvc, int installment, int amount, Integer vat) throws Exception {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(cardNumber)
                .validity(validity)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build();

        assertRequest(paymentRequest, status().isOk());
    }

    @Test
    void postPaymentVatNullTest() throws Exception {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber("0123456789")
                .validity("0720")
                .cvc("032")
                .installment(4)
                .amount(20000)
                .build();

        assertRequest(paymentRequest, status().isOk(), jsonPath("uid").isString());
    }


    private void assertRequest(PaymentRequest paymentRequest, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/v1/payment")
                        .content(new ObjectMapper().writeValueAsString(paymentRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }
    }

    private static Stream<Arguments> provideBadRequestDto() { // argument source method
        return Stream.of(
                Arguments.of("012345678", "0720", "032", 4, 20000, 100),
                Arguments.of("0123456789", "0719", "032", 4, 20000, 100),
                Arguments.of("0123456789", "07193", "032", 4, 20000, 100),
                Arguments.of("0123456789", "0720", "4132", 4, 20000, 100),
                Arguments.of("0123456789", "0720", "032", 1, 20000, 100),
                Arguments.of("0123456789", "0720", "032", 4, 10, 100),
                Arguments.of("0123456789", "0720", "032", 4, 20000, -1),
                Arguments.of("0123456789", "0720", "032", 4, 20000, 20001));
    }

    @ParameterizedTest
    @MethodSource("provideBadRequestDto")
    void paymentValidationCheck(String cardNumber, String validity, String cvc, int installment, int amount, Integer vat) throws Exception {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .cardNumber(cardNumber)
                .validity(validity)
                .cvc(cvc)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build();

        assertRequest(paymentRequest, status().isBadRequest());
    }

}
