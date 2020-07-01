package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.CancelRequest;
import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class PaymentController {


    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UidResponse> addPayment(@RequestBody PaymentRequest paymentRequest) throws BadRequestException {
        return ResponseEntity.ok(new UidResponse(paymentService.requestPayment(paymentRequest.getPayment())));
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public ResponseEntity<UidResponse> addCancel(@PathVariable Uid id,
                                                 @RequestBody CancelRequest cancelRequest) throws BadRequestException {
        cancelRequest.getCancel(id);
        return ResponseEntity.ok(new UidResponse(id));
    }
}
