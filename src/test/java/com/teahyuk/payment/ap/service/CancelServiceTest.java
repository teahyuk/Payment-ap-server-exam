package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumberTest;
import com.teahyuk.payment.ap.domain.vo.card.CvcTest;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.CancelRepository;
import com.teahyuk.payment.ap.repository.CardCompanyRepository;
import com.teahyuk.payment.ap.repository.PaymentRepository;
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
class CancelServiceTest {

    @Autowired
    private CardCompanyRepository cardCompanyRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    private CardCompanyService cardCompanyService;

    private CancelService cancelService;

    @BeforeEach
    void setting() {
        cardCompanyService = new CardCompanyService(cardCompanyRepository);
        cancelService = new CancelService(cardCompanyService, paymentRepository, cancelRepository);
    }

    @Test
    void requestCancelNotFoundTest() {
        Cancel cancel = buildCancel(UidTest.createTestUid("notFoundOriginUid"), 2000, 30);

        StatusResponse<Uid> cancelResponse = cancelService.requestCancel(cancel);

        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.NOT_FOUND);
        System.out.println(cancelResponse.getErrMessage());
    }

    @Test
    void requestCancelTest() {
        //given
        Uid originUid = savePaymentAndGetUid(11000, 100);
        Cancel cancel = buildCancel(originUid, 1000, 30);

        //when
        StatusResponse<Uid> cancelResponse = cancelService.requestCancel(cancel);

        //then
        assertThat(cancelResponse.getData())
                .isNotEqualTo(originUid);

        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.SUCCESS);

        assertThat(cardCompanyRepository.findByUid(cancelResponse.getData().getUid()))
                .isNotEmpty();

        assertThat(cancelRepository.findByUid(cancelResponse.getData().getUid()))
                .isNotEmpty()
                .map(CancelEntity::getPayment)
                .isEqualTo(paymentRepository.findByUid(originUid.getUid()));
    }

    private Uid savePaymentAndGetUid(int amount, int vat) {
        Payment payment = Payment.builder()
                .cardInfo(CardInfo.builder()
                        .cardNumber(CardNumberTest.cardNumber1)
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(CvcTest.cvc1)
                        .build())
                .installment(Installment.of(0))
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .build();
        Uid uid = cardCompanyService.requestToCardCompany(payment.getRequestToCompanyObject());
        paymentRepository.saveAndFlush(payment.toEntity(uid));
        return uid;
    }

    @Test
    void requestCancelBadRequestTest() {
        //given
        Uid insertedUid = savePaymentAndGetUid(110, 100);
        Cancel cancel = buildCancel(insertedUid, 111, 10);

        //when
        StatusResponse<Uid> cancelResponse = cancelService.requestCancel(cancel);

        //then
        assertThat(cancelResponse.getStatusCode())
                .isEqualTo(ProvideStatusCode.BAD_REQUEST);
        System.out.println(cancelResponse.getErrMessage());
    }

    private Cancel buildCancel(Uid insertedUid, int amount, int vat) {
        return Cancel.builder()
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .originUid(insertedUid)
                .build();
    }
}
