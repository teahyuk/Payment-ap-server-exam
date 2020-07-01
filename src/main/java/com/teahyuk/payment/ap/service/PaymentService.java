package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.repository.PaymentStatusRepository;
import com.teahyuk.payment.ap.util.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentStatusRepository PaymentStatusRepository;
    private final CardCompanyService cardCompanyService;

    @Autowired
    public PaymentService(PaymentStatusRepository PaymentStatusRepository, CardCompanyService cardCompanyService) {
        this.PaymentStatusRepository = PaymentStatusRepository;
        this.cardCompanyService = cardCompanyService;
    }

    public Uid requestPayment(Payment payment) throws CryptoException {
        Uid insertedUid = cardCompanyService.requestToCardCompany(payment.getCardCompanyDto());
        PaymentStatusRepository.saveAndFlush(payment.getPaymentStatus(insertedUid));

        return insertedUid;
    }
}
