package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.Uid;
import com.teahyuk.payment.ap.entity.Cancel;
import com.teahyuk.payment.ap.entity.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.teahyuk.payment.ap.domain.UidTest.createTestUid;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {
    private static final String dummyCardInfo = "encryptedCardInfo1encryptedCardInfo1encryptedCardInfo1encryptedCardInfo1";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Test
    void testCurrentAmount() {
        Payment payment = saveAndGetAmount(createTestUid("0"), 20000, 0);
        Cancel cancel1 = saveCancel(payment, createTestUid("1"), 1000, 0);
        Cancel cancel2 = saveCancel(payment, createTestUid("2"), 2000, 0);

        assertThat(paymentRepository.getCurrentPayment(payment.getUid().getUid()))
                .isEqualTo(Optional.of(payment.getAmount() - cancel1.getAmount() - cancel2.getAmount()));
    }

    @Test
    void testCurrentVat() {
        Payment payment = saveAndGetAmount(createTestUid("0"), 20000, 20);
        Cancel cancel1 = saveCancel(payment, createTestUid("1"), 1000, 10);
        Cancel cancel2 = saveCancel(payment, createTestUid("2"), 2000, 5);

        assertThat(paymentRepository.getCurrentVat(payment.getUid().getUid()))
                .isEqualTo(Optional.of(payment.getVat() - cancel1.getVat() - cancel2.getVat()));
    }

    private Cancel saveCancel(Payment payment, Uid uid, int amount, int vat) {
        Cancel cancel = Cancel.builder()
                .amount(amount)
                .cardInfo(payment.getCardInfo())
                .uid(uid)
                .payment(payment)
                .vat(vat)
                .build();
        cancelRepository.save(cancel);
        return cancel;
    }

    private Payment saveAndGetAmount(Uid uid, int amount, int vat) {
        Payment payment = Payment.builder()
                .uid(uid)
                .amount(amount)
                .cardInfo(dummyCardInfo)
                .vat(vat)
                .build();
        paymentRepository.save(payment);
        return payment;
    }
}
