package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.entity.Cancel;
import com.teahyuk.payment.ap.domain.entity.Payment;
import com.teahyuk.payment.ap.domain.uid.Uid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.teahyuk.payment.ap.domain.uid.UidTest.createTestUid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

@DataJpaTest
class PaymentRepositoryTest {
    private static final String dummyCardInfo = "encryptedCardInfo1encryptedCardInfo1encryptedCardInfo1encryptedCardInfo1";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Test
    void testCurrentAmount() {
        Payment payment = saveAndGetPayment(createTestUid("0"), 20000, 0);
        Cancel cancel1 = saveCancel(payment, createTestUid("1"), 1000, 0);
        Cancel cancel2 = saveCancel(payment, createTestUid("2"), 2000, 0);

        assertThat(paymentRepository.getCurrentAmount(payment.getUid()))
                .isEqualTo(Optional.of(payment.getAmount() - cancel1.getAmount() - cancel2.getAmount()));
    }

    @Test
    void testCurrentVat() {
        Payment payment = saveAndGetPayment(createTestUid("0"), 20000, 20);
        Cancel cancel1 = saveCancel(payment, createTestUid("1"), 1000, 10);
        Cancel cancel2 = saveCancel(payment, createTestUid("2"), 2000, 5);

        assertThat(paymentRepository.getCurrentVat(payment.getUid()))
                .isEqualTo(Optional.of(payment.getVat() - cancel1.getVat() - cancel2.getVat()));
    }

    private Cancel saveCancel(Payment payment, Uid uid, int amount, int vat) {
        Cancel cancel = Cancel.builder()
                .amount(new Amount(amount))
                .uid(uid)
                .payment(payment)
                .vat(new Vat(vat))
                .build();
        cancelRepository.save(cancel);
        return cancel;
    }

    private Payment saveAndGetPayment(Uid uid, int amount, int vat) {
        Payment payment = Payment.builder()
                .uid(uid)
                .amount(new Amount(amount))
                .vat(new Vat(vat))
                .build();
        paymentRepository.save(payment);
        return payment;
    }
}
