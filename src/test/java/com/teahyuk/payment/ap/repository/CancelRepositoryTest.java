package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.ValidityTest;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CancelRepositoryTest {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Test
    void getCanceledPricesTest() {
        PaymentEntity payment = saveAndGetAmount("0000000000", 20000, 300);
        CancelEntity cancel1 = saveCancelEntity(payment, "11111111111111111111", 1000, 10);
        CancelEntity cancel2 = saveCancelEntity(payment, "22222222222222222222", 2000, 90);

        //then
        Optional<Tuple> resultTuple = cancelRepository.getCanceledPrices(payment.getUid());
        assertThat(resultTuple)
                .isNotEmpty()
                .map(tuple -> getValue(tuple, "amount"))
                .get()
                .isEqualTo(3000);

        assertThat(resultTuple)
                .isNotEmpty()
                .map(tuple -> getValue(tuple, "vat"))
                .get()
                .isEqualTo(100);
    }

    private int getValue(Tuple tuple, String key) {
        return ((BigInteger) tuple.get(key)).intValue();
    }

    private CancelEntity saveCancelEntity(PaymentEntity payment, String uid, int amount, int vat) {
        CancelEntity cancel = CancelEntity.builder()
                .payment(payment)
                .uid(UidTest.createTestUid(uid))
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .build();
        cancelRepository.save(cancel);
        return cancel;
    }

    private PaymentEntity saveAndGetAmount(String uid, int amount, int vat) {
        PaymentEntity payment = PaymentEntity.builder()
                .uid(UidTest.createTestUid(uid))
                .cardInfo(CardInfo.builder()
                        .cardNumber(new CardNumber("0123456789"))
                        .validity(ValidityTest.thisMonthValidity)
                        .cvc(new Cvc("315"))
                        .build())
                .installment(Installment.of(0))
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .build();
        paymentRepository.save(payment);
        return payment;
    }
}
