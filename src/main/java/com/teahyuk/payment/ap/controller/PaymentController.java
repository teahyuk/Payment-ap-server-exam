package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {
    //TODO status-code 409 conflict
    //TODO 500

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UidResponse> addPayment(@RequestBody PaymentRequest paymentRequest) throws BadRequestException {
        return ResponseEntity.ok(new UidResponse(paymentService.requestPayment(paymentRequest.getPaymentRequest())));
    }

}
