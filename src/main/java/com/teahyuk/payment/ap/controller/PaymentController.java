package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.CancelRequest;
import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import com.teahyuk.payment.ap.dto.response.PaymentResponse;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
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
    private final CancelService cancelService;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository, CancelService cancelService) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
        this.cancelService = cancelService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<UidResponse> addPayment(@RequestBody PaymentRequest paymentRequest) throws BadRequestException {
        return ResponseEntity.ok(new UidResponse(paymentService.requestPayment(paymentRequest.getPayment())));
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public ResponseEntity<?> addCancel(@PathVariable Uid id,
                                       @RequestBody CancelRequest cancelRequest) throws BadRequestException {
        StatusResponse<Uid> statusResponse = cancelService.requestCancel(cancelRequest.getCancel(id));
        return statusResponse
                .map(UidResponse::new)
                .responseEntity();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Uid id) {
        return paymentRepository.findByUid(id.getUid())
                .map(PaymentResponse::fromEntity)
                .map(ResponseEntity.ok()::body)
                .orElseGet(() ->
                        ResponseEntity.notFound()
                                .build());
    }
}
