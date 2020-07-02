package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PaymentService {
    private final CardCompanyService cardCompanyService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(CardCompanyService cardCompanyService,
                          PaymentRepository paymentRepository) {
        this.cardCompanyService = cardCompanyService;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Uid requestPayment(Payment payment) {
        Uid insertedUid = cardCompanyService.requestToCardCompany(payment.getRequestToCompanyObject());
        paymentRepository.saveAndFlush(payment.toEntity(insertedUid));

        return insertedUid;
    }
}
