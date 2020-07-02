package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(value = {PaymentController.class, PaymentService.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PaymentApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    PaymentService paymentService;

    private static Stream<Arguments> provideValidParam() {
        return Stream.of(
                Arguments.of("0123456789", "0720", "032", 4, 20000, 100),
                Arguments.of("0123456789", "0720", "032", 4, 20000, null));
    }

    @ParameterizedTest
    @MethodSource("provideValidParam")
    void postSuccessTest(String cardNumber, String validity, String cvc, int installment, int amount, Integer vat) throws Exception {
        Uid expectUid = UidTest.createTestUid("_resultUid_");
        given(paymentService.requestPayment(any()))
                .willReturn(expectUid);

        assertRequest(getRequestMap(cardNumber, validity, cvc, installment, amount, vat),
                status().isOk(),
                jsonPath("uid", is(expectUid.getUid())));
    }

    private static Stream<Arguments> provideInvalidParam() {
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
    @MethodSource("provideInvalidParam")
    void paymentValidationCheck(String cardNumber, String validity, String cvc, Integer installment, Integer amount, Integer vat) throws Exception {
        assertRequest(getRequestMap(cardNumber, validity, cvc, installment, amount, vat),
                status().isBadRequest());
    }

    private Map<String, Object> getRequestMap(String cardNumber, String validity, String cvc, Integer installment,
                                              Integer amount, Integer vat) {
        Map<String, Object> requestMap = new HashMap<>();
        putIfNotNull(cardNumber, requestMap, "cardNumber");
        putIfNotNull(validity, requestMap, "validity");
        putIfNotNull(cvc, requestMap, "cvc");
        putIfNotNull(installment, requestMap, "installment");
        putIfNotNull(amount, requestMap, "amount");
        putIfNotNull(vat, requestMap, "vat");
        return requestMap;
    }

    private void putIfNotNull(Object cardNumber, Map<String, Object> requestMap, String key) {
        if (cardNumber != null) {
            requestMap.put(key, cardNumber);
        }
    }

    private void assertRequest(Object paymentRequest, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/v1/payment")
                        .content(new ObjectMapper().writeValueAsString(paymentRequest))
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }
    }
}
