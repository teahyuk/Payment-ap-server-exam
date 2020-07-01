package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
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
        //given
        RequestToCompanyObject cardCompanyDto = RequestToCompanyObject.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();

        //when
        Uid savedUid = cardCompanyService.requestToCardCompany(cardCompanyDto);

        //then
        assertThat(cardCompanyRepository.findByUid(savedUid.getUid()))
                .isNotEmpty();
    }

    @Test
    void requestDuplicateUidTest() {
        RequestToCompanyObject cardCompanyDto = RequestToCompanyObject.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();

        Uid firstUid = cardCompanyService.requestToCardCompany(cardCompanyDto);
        Uid secondUid = cardCompanyService.requestToCardCompany(cardCompanyDto);

        assertThat(firstUid)
                .isNotEqualTo(secondUid);
    }

    @Test
    void requestToCardCompanyCancelTest() {
        RequestToCompanyObject cardCompanyDto = RequestToCompanyObject.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();

        Uid originUid = cardCompanyService.requestToCardCompany(cardCompanyDto);

        Cancel cancel = Cancel.builder()
                .originUid(originUid)
                .amount(new Amount(555))
                .vat(new Vat(23))
                .build();

        Cancel wrongOriginUidCancel = Cancel.builder()
                .originUid(UidTest.createTestUid("_wrongUid_"))
                .amount(new Amount(555))
                .vat(new Vat(23))
                .build();

        assertThat(cardCompanyService.requestToCardCompany(cancel))
                .isNotEmpty()
                .isNotEqualTo(originUid);

        assertThat(cardCompanyService.requestToCardCompany(wrongOriginUidCancel))
                .isEmpty();
    }
}
