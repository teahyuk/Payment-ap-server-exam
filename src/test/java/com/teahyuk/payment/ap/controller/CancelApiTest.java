package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import com.teahyuk.payment.ap.service.CancelService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(value = {PaymentController.class, PaymentService.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CancelApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CancelService cancelService;

    @MockBean
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentService paymentService;

    private static Stream<Arguments> provideValidParameter() {
        return Stream.of(
                Arguments.of("01234567890123456789", 20000, 100),
                Arguments.of("ba123sd65f4w9e8t7xx5", 20000, null));
    }

    @ParameterizedTest
    @MethodSource("provideValidParameter")
    void postCancelTest(String uid, Integer amount, Integer vat) throws Exception {
        Uid expectUid = UidTest.createTestUid("_resultUid_");
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(expectUid)
                        .statusCode(ProvideStatusCode.SUCCESS)
                        .build());

        assertRequest(uid,
                getRequestMap(amount, vat),
                status().isOk(),
                jsonPath("uid", is(expectUid.getUid())));
    }

    private static Stream<Arguments> provideInvalidParameter() {
        return Stream.of(
                Arguments.of("0123456789012345678", 100, null),
                Arguments.of("01234567890123456789", 100, 1000),
                Arguments.of("01234567890123456789", 100, -1),
                Arguments.of("01234567890123456789", null, 0));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidParameter")
    void paymentValidationCheck(String uid, Integer amount, Integer vat) throws Exception {
        Uid expectUid = UidTest.createTestUid("_resultUid_");
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(expectUid)
                        .statusCode(ProvideStatusCode.SUCCESS)
                        .build());

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
                        .characterEncoding("UTF-8")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }
    }

    @ParameterizedTest
    @MethodSource("provideValidParameter")
    void getNotFoundTest(String uid, Integer amount, Integer vat) throws Exception {
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(UidTest.createTestUid("###"))
                        .statusCode(ProvideStatusCode.NOT_FOUND)
                        .errMessage("not found")
                        .build());

        assertRequest(uid,
                getRequestMap(amount, vat),
                status().isNotFound(),
                content().string(is("not found")));
    }

    @ParameterizedTest
    @MethodSource("provideValidParameter")
    void getBadRequestTest(String uid, Integer amount, Integer vat) throws Exception {
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(UidTest.createTestUid("###"))
                        .statusCode(ProvideStatusCode.BAD_REQUEST)
                        .errMessage("bad request, this payment can not cancelable")
                        .build());

        assertRequest(uid,
                getRequestMap(amount, vat),
                status().isBadRequest(),
                content().string(is("bad request, this payment can not cancelable")));
    }

}
