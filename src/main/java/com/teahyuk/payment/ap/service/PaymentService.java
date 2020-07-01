package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentStatusRepository paymentStatusRepository;
    private final CardCompanyService cardCompanyService;

    @Autowired
    public PaymentService(PaymentStatusRepository PaymentStatusRepository, CardCompanyService cardCompanyService) {
        this.paymentStatusRepository = PaymentStatusRepository;
        this.cardCompanyService = cardCompanyService;
    }

    public Uid requestPayment(Payment payment) {
        Uid insertedUid = cardCompanyService.requestToCardCompany(payment.getRequestToCompanyObject());
        paymentStatusRepository.saveAndFlush(payment.getPaymentStatus(insertedUid));

        return insertedUid;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ) //todo if redis transaction -> remove this
    public StatusResponse<Uid> requestCancel(Cancel cancel) {
        return paymentStatusRepository.findByUid(cancel.getOriginUid().getUid())
                .flatMap(paymentStatus -> {
                    if (!cancel.isCancelableWithSettingVat(paymentStatus)) {//부분 취소 가능한 가격인지?
                        return Optional.of(getBadRequestStatusResponse(cancel, paymentStatus));
                    }
                    Optional<StatusResponse<Uid>> result = cardCompanyService.requestToCardCompany(cancel)
                            .map(this::getSuccessResponse);
                    if (result.isPresent()) {
                        cancel.cancel(paymentStatus);
                    }
                    return result;
                }).orElseGet(() -> getNotFoundResponse(cancel));
    }

    private StatusResponse<Uid> getNotFoundResponse(Cancel cancel) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.NOT_FOUND)
                .errMessage(String.format("not exist origin uid, origin uid = %s",
                        cancel.getOriginUid().getUid()))
                .build();
    }

    private StatusResponse<Uid> getSuccessResponse(Uid uid) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.SUCCESS)
                .data(uid)
                .build();
    }

    private StatusResponse<Uid> getBadRequestStatusResponse(Cancel cancel, PaymentStatus paymentStatus) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.BAD_REQUEST)
                .errMessage(String.format("can not request cancel. cause cancel amount is invalid price. " +
                                "payed amount %d(%d) requested cancel amount %d(%d)",
                        paymentStatus.getAmount(),
                        paymentStatus.getVat(),
                        cancel.getAmount().getAmount(),
                        cancel.getVat().getVat()))
                .build();
    }
}
