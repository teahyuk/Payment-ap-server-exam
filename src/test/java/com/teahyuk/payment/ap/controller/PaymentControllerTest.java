package com.teahyuk.payment.ap.controller;

import lombok.extern.slf4j.Slf4j;
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
        String paymentRequest = getPaymentRequestString(cardNumber, validity, cvc, installment, amount, vat);

        assertRequest(paymentRequest, status().isOk());
    }

    private void assertRequest(String paymentRequest, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/v1/payment")
                        .content(paymentRequest)
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
                Arguments.of("0123456789", "0720", "032", 4, 20000, 20001),
                Arguments.of(null, "0720", "032", 4, 20000, 100),
                Arguments.of("0123456789", null, "032", 4, 20000, 100),
                Arguments.of("0123456789", "0720", null, 4, 20000, 100),
                Arguments.of("0123456789", "0720", "032", null, 20000, 100),
                Arguments.of("0123456789", "0720", "032", 4, null, 100));
    }

    @ParameterizedTest
    @MethodSource("provideBadRequestDto")
    void paymentValidationCheck(String cardNumber, String validity, String cvc, Integer installment, Integer amount, Integer vat) throws Exception {
        String paymentRequest = getPaymentRequestString(cardNumber, validity, cvc, installment, amount, vat);

        assertRequest(paymentRequest, status().isBadRequest());
    }

    private String getPaymentRequestString(String cardNumber, String validity, String cvc, Integer installment, Integer amount, Integer vat) {
        return String.format("{\"cardNumber\":\"%s\",\"validity\":\"%s\",\"cvc\":\"%s\"," +
                "\"installment\":%s,\"amount\":%s,\"vat\":%s}", cardNumber, validity, cvc, installment, amount, vat);
    }

}
