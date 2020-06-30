package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.card.CvcTest;
import com.teahyuk.payment.ap.domain.card.ValidityTest;
import com.teahyuk.payment.ap.domain.entity.CardCompany;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.domain.uid.UidTest;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.dto.card.company.RequestType;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CardCompanyServiceTest {

    @Autowired
    private CardCompanyRepository cardCompanyRepository;

    private CardCompanyService cardCompanyService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
    }


    @Test
    void requestToCardCompanyTest() {
        Uid requestUid = UidTest.createTestUid("312512512");
        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .uid(requestUid)
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();

        assertThat(cardCompanyService.requestToCardCompany(cardCompanyDto))
                .isEqualTo(requestUid);
    }

    @Test
    void requestDuplicateUidTest() {
        Uid requestUid = UidTest.createTestUid("312512512");
        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .uid(requestUid)
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();

        cardCompanyRepository.save(CardCompany.builder()
                .uid(requestUid)
                .string("requestedSerializedData")
                .build());

        assertThat(cardCompanyService.requestToCardCompany(cardCompanyDto))
                .isNotEqualTo(requestUid);
    }
}
