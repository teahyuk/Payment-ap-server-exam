package com.teahyuk.payment.ap.domain;

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

}
