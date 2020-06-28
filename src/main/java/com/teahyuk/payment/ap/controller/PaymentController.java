package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {

    //TODO 서비스 구현
    //TODO Repository 구현
    @PostMapping
    public Mono<?> addPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        logger.info("receivedRequest");
        logger.warn(paymentRequest.toString());
        return Mono.just(paymentRequest);
    }
}
