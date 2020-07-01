package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CancelApiTest {

    @Autowired
    private MockMvc mvc;

    private static Stream<Arguments> provideSuccessDto() { // argument source method
        return Stream.of(
                Arguments.of("01234567890123456789", 20000, 100),
                Arguments.of("ba123sd65f4w9e8t7xx5", 20000, null));
    }

    @ParameterizedTest
    @MethodSource("provideSuccessDto")
    void postPaymentTest(String uid, Integer amount, Integer vat) throws Exception {
        assertRequest(uid,
                getRequestMap(amount, vat),
                status().isOk(),
                jsonPath("uid").isString());
    }

    private static Stream<Arguments> provideBadRequestDto() { // argument source method
        return Stream.of(
                Arguments.of("0123456789012345678", 100, null),
                Arguments.of("01234567890123456789",100, 1000),
                Arguments.of("01234567890123456789",100, -1),
                Arguments.of("01234567890123456789",null, 0));
    }

    @ParameterizedTest
    @MethodSource("provideBadRequestDto")
    void paymentValidationCheck(String uid, Integer amount, Integer vat) throws Exception {
        assertRequest(uid,
                getRequestMap(amount, vat),
                status().isBadRequest());
    }

    private Map<String, Object> getRequestMap(Integer amount, Integer vat) {
        Map<String, Object> requestMap = new HashMap<>();
        putIfNotNull(amount, requestMap, "amount");
        putIfNotNull(vat, requestMap, "vat");
        return requestMap;
    }

    private void putIfNotNull(Object cardNumber, Map<String, Object> requestMap, String key) {
        if (cardNumber != null) {
            requestMap.put(key, cardNumber);
        }
    }

    private void assertRequest(String uid, Object request, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mvc.perform(
                post(String.format("/v1/payment/%s/cancel", uid))
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }
    }

}
