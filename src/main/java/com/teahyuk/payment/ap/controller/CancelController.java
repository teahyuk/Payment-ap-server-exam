package com.teahyuk.payment.ap.controller;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.request.CancelRequest;
import com.teahyuk.payment.ap.dto.response.PaymentResponse;
import com.teahyuk.payment.ap.dto.response.UidResponse;
import com.teahyuk.payment.ap.exception.BadRequestException;
import com.teahyuk.payment.ap.repository.CancelRepository;
import com.teahyuk.payment.ap.service.CancelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{uid}/cancel")
    @ResponseBody
    public ResponseEntity<?> addCancel(@PathVariable Uid uid,
                                       @RequestBody CancelRequest cancelRequest) throws BadRequestException {
        return cancelService.requestCancel(cancelRequest.getCancel(uid))
                .map(UidResponse::new)
                .responseEntity();
    }

    @GetMapping("/{paymentUid}/cancel/{cancelUid}")
    @ResponseBody
    public ResponseEntity<PaymentResponse> getCancel(@PathVariable Uid paymentUid,
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
