package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CancelTest {

    @Test
    void getRequestToCompanyObjectTest() {
        Cancel cancel = Cancel.builder()
                .amount(new Amount(20000))
                .vat(new Vat(300))
                .originUid(UidTest.createTestUid("_originUid_"))
                .build();
        CardInfo cardInfo = CardInfo.builder()
                .cardNumber(CardNumberTest.cardNumber1)
                .cvc(CvcTest.cvc1)
                .validity(ValidityTest.thisMonthValidity)
                .build();

        RequestToCompanyObject requestToCompanyObject = cancel.getRequestToObject(cardInfo);

        assertThat(requestToCompanyObject)
                .isEqualTo(RequestToCompanyObject.builder()
                        .cardInfo(cardInfo)
                        .requestType(RequestType.CANCEL)
                        .amount(cancel.getAmount())
                        .vat(cancel.getVat())
                        .installment(Installment.of(0))
                        .originUid(cancel.getOriginUid())
                        .build());

    }

    private static Stream<Arguments> provideIsCancelableTest() {
        return Stream.of(
                Arguments.of(11000, 1000, 1100, 100, true),
                Arguments.of(9900, 900, 3300, null, true),
                Arguments.of(6600, 600, 7000, null, false),
                Arguments.of(6600, 600, 6600, 700, false),
                Arguments.of(6600, 600, 6600, 600, true),
                Arguments.of(0, 0, 100, null, false),

                Arguments.of(20000, 909, 10000, 0, true),
                Arguments.of(10000, 909, 10000, 0, false),
                Arguments.of(10000, 909, 10000, 909, true),

                Arguments.of(20000, 1818, 10000, 1000, true),
                Arguments.of(10000, 818, 10000, 909, false),
                Arguments.of(10000, 818, 10000, null, true));
    }

    @ParameterizedTest
    @MethodSource("provideIsCancelableTest")
    void isCancelableTest(int originAmount, int originVat, int cancelAmount, Integer cancelVat, boolean expect) {
        assertThat(Cancel.builder()
                .amount(new Amount(cancelAmount))
                .vat(cancelVat == null ? null : new Vat(cancelVat))
                .build().settingVatAndCheckCancelable(
                        PaymentStatus.builder()
                                .amount(originAmount)
                                .vat(originVat)
                                .uid(UidTest.createTestUid("_cancelTestUid_"))
                                .build()))
                .isEqualTo(expect);
    }

    private static Stream<Arguments> provideCancelTest() {
        return Stream.of(
                Arguments.of(11000, 1000, 1100, 100, 9900, 900),
                Arguments.of(9900, 900, 3300, null, 6600, 600),
                // Arguments.of(6600, 600, 7000, null, false),
                // Arguments.of(6600, 600, 6600, 700, false),
                Arguments.of(6600, 600, 6600, 600, 0, 0),
                // Arguments.of(0, 0, 100, null, false),

                Arguments.of(20000, 909, 10000, 0, 10000, 909),
                //Arguments.of(10000, 909, 10000, 0, false),
                Arguments.of(10000, 909, 10000, 909, 0, 0),

                Arguments.of(20000, 1818, 10000, 1000, 10000, 818),
                //Arguments.of(10000, 818, 10000, 909, false),
                Arguments.of(10000, 818, 10000, null, 0, 0));
    }

    @ParameterizedTest
    @MethodSource("provideCancelTest")
    void cancelTest(int originAmount, int originVat, int cancelAmount, Integer cancelVat, int expectAmount,
                    int expectVat) {
        PaymentStatus paymentStatus = PaymentStatus.builder()
                .amount(originAmount)
                .vat(originVat)
                .uid(UidTest.createTestUid("_cancelTestUid_"))
                .build();

        //when
        Cancel.builder()
                .amount(new Amount(cancelAmount))
                .vat(cancelVat == null ? null : new Vat(cancelVat))
                .build().cancel(paymentStatus);

        //then
        assertThat(paymentStatus.getAmount())
                .isEqualTo(expectAmount);
        assertThat(paymentStatus.getVat())
                .isEqualTo(expectVat);
    }
}
