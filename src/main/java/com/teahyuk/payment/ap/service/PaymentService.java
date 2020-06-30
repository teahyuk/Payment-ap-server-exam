package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.entity.Payment;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.dto.PaymentRequest;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import com.teahyuk.payment.ap.util.CryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CardCompanyService cardCompanyService;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CardCompanyService cardCompanyService) {
        this.paymentRepository = paymentRepository;
        this.cardCompanyService = cardCompanyService;
    }

    public Uid requestPayment(PaymentRequest paymentRequest) throws CryptoException {
        Uid insertedUid = cardCompanyService.requestToCardCompany(paymentRequest.getCardCompanyDto());
        paymentRepository.saveAndFlush(Payment.builder()
                .uid(insertedUid)
                .amount(paymentRequest.getAmount())
                .vat(paymentRequest.getVat())
                .build());

        return insertedUid;
    }
}
