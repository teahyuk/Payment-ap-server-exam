package com.teahyuk.payment.ap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.CancelRepository;
import com.teahyuk.payment.ap.service.CancelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = {CancelController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ActiveProfiles("test")
class CancelApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CancelService cancelService;

    @MockBean
    private CancelRepository cancelRepository;

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

        assertPostRequest(uid,
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
    void postValidationCheckTest(String uid, Integer amount, Integer vat) throws Exception {
        Uid expectUid = UidTest.createTestUid("_resultUid_");
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(expectUid)
                        .statusCode(ProvideStatusCode.SUCCESS)
                        .build());

        assertPostRequest(uid,
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

    private void assertPostRequest(String uid, Object request, ResultMatcher... resultMatchers) throws Exception {
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
    void postNotFoundTest(String uid, Integer amount, Integer vat) throws Exception {
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(UidTest.createTestUid("###"))
                        .statusCode(ProvideStatusCode.NOT_FOUND)
                        .errMessage("not found")
                        .build());

        assertPostRequest(uid,
                getRequestMap(amount, vat),
                status().isNotFound(),
                content().string(is("not found")));
    }

    @ParameterizedTest
    @MethodSource("provideValidParameter")
    void postBadRequestTest(String uid, Integer amount, Integer vat) throws Exception {
        given(cancelService.requestCancel(any()))
                .willReturn(StatusResponse.<Uid>builder()
                        .data(UidTest.createTestUid("###"))
                        .statusCode(ProvideStatusCode.BAD_REQUEST)
                        .errMessage("bad request, this payment can not cancelable")
                        .build());

        assertPostRequest(uid,
                getRequestMap(amount, vat),
                status().isBadRequest(),
                content().string(is("bad request, this payment can not cancelable")));
    }

    @Test
    void getCancelTest() throws Exception {
        Uid originalUid = UidTest.createTestUid("originUid");
        Uid cancelUid = UidTest.createTestUid("cancelUid");
        settingCancelEntityReturn(originalUid, cancelUid);

        assertGetRequest(originalUid.getUid(), cancelUid.getUid(),
                status().isOk(),
                jsonPath("uid", is(cancelUid.getUid())));
    }

    @Test
    void getCancelNotFoundTest() throws Exception {
        Uid originalUid = UidTest.createTestUid("originUid");
        Uid cancelUid = UidTest.createTestUid("cancelUid");
        settingCancelEntityReturn(originalUid, cancelUid);

        assertGetRequest("a1s2d3f4a5s6d7f8d9s0", cancelUid.getUid(),
                status().isNotFound());
    }

    private void settingCancelEntityReturn(Uid originalUid, Uid cancelUid) {
        given(cancelRepository.findByUid(any()))
                .willReturn(Optional.of(CancelEntity.builder()
                        .amount(new Amount(1000))
                        .vat(new Vat(200))
                        .payment(PaymentEntity.builder()
                                .uid(originalUid)
                                .cardInfo(CardInfo.builder()
                                        .cardNumber(new CardNumber("12341234252"))
                                        .validity(ValidityTest.thisMonthValidity)
                                        .cvc(CvcTest.cvc1)
                                        .build())
                                .amount(new Amount(420000))
                                .vat(new Vat(402))
                                .installment(Installment.of(0))
                                .build())
                        .uid(cancelUid)
                        .build()));
    }

    private void assertGetRequest(String originUid, String cancelUid, ResultMatcher... resultMatchers) throws Exception {
        ResultActions resultActions = mvc.perform(
                get(String.format("/v1/payment/%s/cancel/%s", originUid, cancelUid))
                        .characterEncoding("UTF-8"))
                .andDo(print());

        for (ResultMatcher resultMatcher : resultMatchers) {
            resultActions.andExpect(resultMatcher);
        }
    }
}
