package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import com.teahyuk.payment.ap.dto.response.PaymentResponse;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import com.teahyuk.payment.ap.service.CancelService;
import com.teahyuk.payment.ap.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository, CancelService cancelService) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    public ResponseEntity<UidResponse> addPayment(@RequestBody PaymentRequest paymentRequest) throws BadRequestException {
        return ResponseEntity.ok(new UidResponse(paymentService.requestPayment(paymentRequest.getPayment())));
    }

    @GetMapping("/{uid}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Uid uid) {
        return paymentRepository.findByUid(uid.getUid())
                .map(PaymentResponse::fromPayment)
                .map(ResponseEntity.ok()::body)
                .orElseGet(() ->
                        ResponseEntity.notFound()
                                .build());
    }
}
