package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class CardCompanyTest {

    @Test
    void getCardInfoTest() {
        //given
        CardInfo expectCardInfo = CardInfo.builder()
                .cardNumber(new CardNumber("0123456789"))
                .validity(new Validity("0521"))
                .cvc(new Cvc("331"))
                .build();
        RequestToCompanyObject companyObject = RequestToCompanyObject.builder()
                .cardInfo(expectCardInfo)
                .amount(new Amount(11000))
                .vat(new Vat(1000))
                .installment(Installment.of(0))
                .requestType(RequestType.PAYMENT)
                .build();

        CardCompany cardCompany = companyObject.toEntity(UidTest.createTestUid("_testUid_"));

        //when
        assertThat(cardCompany.getCardInfo())
                .isEqualTo(expectCardInfo);
    }

}
