package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.PaymentRequest;
import com.teahyuk.payment.ap.dto.response.PaymentResponse;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import com.teahyuk.payment.ap.service.CancelService;
import com.teahyuk.payment.ap.service.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "1. payment")
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentController(PaymentService paymentService, PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @ApiOperation(value = "결제 요청", notes = "카드사로 결제 요청을 수행 한다.")
    @PostMapping
    public ResponseEntity<UidResponse> addPayment(@ApiParam(value = "결제 정보", required = true)
                                                      @RequestBody PaymentRequest paymentRequest) throws BadRequestException {
        return ResponseEntity.ok(new UidResponse(paymentService.requestPayment(paymentRequest.getPayment())));
    }

    @ApiOperation(value = "결제 내역 확인", notes = "uid 에 맞는 결제 내역을 조회 한다.")
    @GetMapping("/{uid}")
    public ResponseEntity<PaymentResponse> getPayment(@ApiParam(value = "결제 id", required = true)
                                                          @PathVariable Uid uid) {
        return paymentRepository.findByUid(uid.getUid())
                .map(PaymentResponse::fromPayment)
                .map(ResponseEntity.ok()::body)
                .orElseGet(() ->
                        ResponseEntity.notFound()
                                .build());
    }
}
