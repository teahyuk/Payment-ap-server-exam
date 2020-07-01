package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.entity.PaymentState;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.repository.PaymentStateRepository;
import com.teahyuk.payment.ap.util.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentStateRepository paymentStateRepository;
    private final CardCompanyService cardCompanyService;

    @Autowired
    public PaymentService(PaymentStateRepository paymentStateRepository, CardCompanyService cardCompanyService) {
        this.paymentStateRepository = paymentStateRepository;
        this.cardCompanyService = cardCompanyService;
    }

    public Uid requestPayment(Payment payment) throws CryptoException {
        Uid insertedUid = cardCompanyService.requestToCardCompany(payment.getCardCompanyDto());
        paymentStateRepository.saveAndFlush(payment.getPaymentState(insertedUid));

        return insertedUid;
    }
}
