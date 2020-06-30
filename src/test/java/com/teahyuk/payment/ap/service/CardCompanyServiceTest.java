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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardCompanyServiceTest {

    @Mock
    CardCompanyRepository cardCompanyRepository;

    @InjectMocks
    CardCompanyService cardCompanyService;

    @Test
    void requestToCardCompanyTest() {
        CardCompanyDto cardCompanyDto = CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .uid(UidTest.createTestUid("312512512"))
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .amount(new Amount(555))
                .vat(new Vat(23))
                .installment(Installment.of(11))
                .build();
        Uid returnUid = UidTest.createTestUid("5235");
        when(cardCompanyRepository.saveAndFlush(any()))
                .thenReturn(CardCompany.builder()
                        .uid(returnUid)
                        .build());

        assertThat(cardCompanyService.requestToCardCompany(cardCompanyDto))
                .isEqualTo(returnUid);
    }
}
