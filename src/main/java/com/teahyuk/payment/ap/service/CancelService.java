package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.RemainingPrice;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.CancelRepository;
import com.teahyuk.payment.ap.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class CancelService {
    public static final String CAN_NOT_CANCEL_LOG_FORMAT = "Can not cancel request, \n" +
            "cancel request is greater then recent payment amount: " +
            "cancel={}, remainingPrice={}";
    public static final String NOT_FOUND_RESPONSE_FORMAT = "Not exist payment data in origin uid, origin uid = %s";
    public static final String BAD_REQUEST_RESPONSE_FORMAT = "Can not request cancel. cause cancel is invalid price. " +
            "payed amount %d(%d) requested cancel amount %d(%d)";

    private final CardCompanyService cardCompanyService;
    private final PaymentRepository paymentRepository;
    private final CancelRepository cancelRepository;

    @Autowired
    public CancelService(CardCompanyService cardCompanyService,
                         PaymentRepository paymentRepository,
                         CancelRepository cancelRepository) {
        this.cardCompanyService = cardCompanyService;
        this.paymentRepository = paymentRepository;
        this.cancelRepository = cancelRepository;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public StatusResponse<Uid> requestCancel(Cancel cancel) {
        String originUid = cancel.getOriginUid().getUid();
        Optional<PaymentEntity> paymentEntity = paymentRepository.findByUid(originUid);

        if (!paymentEntity.isPresent()) {
            return getNotFoundResponse(cancel);
        }

        RemainingPrice remainingPrice = paymentEntity.get().getRemainingPrice();
        remainingPrice = cancelRepository.getCanceledPrices(originUid)
                .map(remainingPrice::subtract)
                .orElse(remainingPrice);

        if (isNotCancelable(cancel, remainingPrice)) { //남은 가격 에서 취소 가능 한지 확인
            return getBadRequest(cancel, remainingPrice);
        }

        return cardCompanyService.requestToCardCompany(cancel) //카드사 데이터에 넣고
                .map(cancelUid -> {
                    paymentEntity
                            .map(payment -> cancel.toEntity(payment, cancelUid))
                            .ifPresent(cancelRepository::saveAndFlush); // 취소 테이블 에 넣는다.
                    return getSuccessResponse(cancelUid);
                })
                .orElseGet(() ->
                        getNotFoundResponse(cancel));
    }

    private boolean isNotCancelable(Cancel cancel, RemainingPrice remainingPrice) {
        cancel.settingVat(remainingPrice.getVat());
        return !cancel.isCancelable(remainingPrice);
    }

    private StatusResponse<Uid> getBadRequest(Cancel cancel, RemainingPrice remainingPrice) {
        logger.warn(CAN_NOT_CANCEL_LOG_FORMAT, cancel, remainingPrice);
        return getBadRequestStatusResponse(cancel, remainingPrice);
    }

    private StatusResponse<Uid> getNotFoundResponse(Cancel cancel) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.NOT_FOUND)
                .errMessage(String.format(NOT_FOUND_RESPONSE_FORMAT,
                        cancel.getOriginUid().getUid()))
                .build();
    }

    private StatusResponse<Uid> getSuccessResponse(Uid uid) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.SUCCESS)
                .data(uid)
                .build();
    }

    private StatusResponse<Uid> getBadRequestStatusResponse(Cancel cancel, RemainingPrice remainingPrice) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.BAD_REQUEST)
                .errMessage(String.format(BAD_REQUEST_RESPONSE_FORMAT,
                        remainingPrice.getAmount().getAmount(),
                        remainingPrice.getVat().getVat(),
                        cancel.getAmount().getAmount(),
                        cancel.getVat().getVat()))
                .build();
    }
}
