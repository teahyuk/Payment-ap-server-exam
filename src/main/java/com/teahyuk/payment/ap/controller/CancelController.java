package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.CancelRequest;
import com.teahyuk.payment.ap.dto.response.PaymentResponse;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.repository.CancelRepository;
import com.teahyuk.payment.ap.service.CancelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "2. cancel")
@RestController
@RequestMapping("/v1/payment")
@Slf4j
public class CancelController {
    private final CancelService cancelService;
    private final CancelRepository cancelRepository;

    @Autowired
    public CancelController(CancelService cancelService, CancelRepository cancelRepository) {
        this.cancelService = cancelService;
        this.cancelRepository = cancelRepository;
    }

    @ApiOperation(value = "취소 요청",
            notes = "결제 취소를 요청 한다.")
    @PostMapping("/{uid}/cancel")
    public ResponseEntity<?> addCancel(@ApiParam(value = "결제 id", required = true)
                                       @PathVariable Uid uid,
                                       @ApiParam(value = "취소 정보", required = true)
                                       @RequestBody CancelRequest cancelRequest) throws BadRequestException {
        return cancelService.requestCancel(cancelRequest.getCancel(uid))
                .map(UidResponse::new)
                .responseEntity();
    }

    @ApiOperation(value = "취소 내역 확인",
            notes = "uid 에 맞는 결제 취소 내역을 조회 한다.")
    @GetMapping("/{paymentUid}/cancel/{cancelUid}")
    public ResponseEntity<PaymentResponse> getCancel(@ApiParam(value = "결제 id", required = true)
                                                     @PathVariable Uid paymentUid,
                                                     @ApiParam(value = "결제 취소 id", required = true)
                                                     @PathVariable Uid cancelUid) {
        return cancelRepository.findByUid(cancelUid.getUid())
                .filter(v -> v.getPayment().getUid().equals(paymentUid.getUid()))
                .map(PaymentResponse::fromCancel)
                .map(ResponseEntity.ok()::body)
                .orElseGet(() ->
                        ResponseEntity.notFound()
                                .build());
    }
}
