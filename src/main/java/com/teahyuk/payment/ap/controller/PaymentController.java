package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {

    //TODO 서비스 구현
    //TODO Repository 구현
    @PostMapping
    @ResponseBody
    public ResponseEntity<?> addPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        logger.info("receivedRequest");
        logger.warn(paymentRequest.toString());
        return ResponseEntity.ok(paymentRequest);
    }
}
