package com.teahyuk.payment.ap.service;

import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.StatusResponse;
import com.teahyuk.payment.ap.repository.PaymentStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PaymentService {
    public static final String CAN_NOT_CANCEL_LOG_FORMAT = "Can not cancel in paymentStatus, because cancel prices must less eq " +
            "paymentStatus and canceled vat must be less eq canceled amount, " +
            "cancel={}, paymentStatus={}";
    public static final String NOT_FOUND_PAYMENT_STATUS_FROM_LOG_FORMAT = "Not found paymentStatusEntity from cancel original uid, originalUid={}";
    public static final String NOT_FOUND_RESPONSE_FORMAT = "not exist origin uid, origin uid = %s";
    public static final String BAD_REQUEST_REPONSE_FORMAT = "Can not request cancel. cause cancel is invalid price. " +
            "payed amount %d(%d) requested cancel amount %d(%d)";
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

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public StatusResponse<Uid> requestCancel(Cancel cancel) {
        return paymentStatusRepository.findByUid(cancel.getOriginUid().getUid())
                .map(paymentStatus ->
                        requestCardCompanyAndUpdatePaymentStatus(cancel, paymentStatus))
                .orElseGet(() ->
                        getPaymentStatusNotFound(cancel));
    }

    private StatusResponse<Uid> requestCardCompanyAndUpdatePaymentStatus(Cancel cancel, PaymentStatus paymentStatus) {
        if (!cancel.settingVatAndCheckCancelable(paymentStatus)) {
            return loggingAndGetBadRequestResponse(cancel, paymentStatus);
        }
        return updateIfRequestSuccess(cancel, paymentStatus, requestToCardCompany(cancel));
    }

    private StatusResponse<Uid> loggingAndGetBadRequestResponse(Cancel cancel, PaymentStatus paymentStatus) {
        logger.warn(CAN_NOT_CANCEL_LOG_FORMAT, cancel, paymentStatus);
        return getBadRequestStatusResponse(cancel, paymentStatus);
    }

    private StatusResponse<Uid> updateIfRequestSuccess(Cancel cancel, PaymentStatus paymentStatus, StatusResponse<Uid> result) {
        if (result.isSuccess()) {
            cancel.cancel(paymentStatus);
        }
        return result;
    }

    private StatusResponse<Uid> requestToCardCompany(Cancel cancel) {
        return cardCompanyService.requestToCardCompany(cancel)
                .map(this::getSuccessResponse)
                .orElseGet(() -> getNotFoundResponse(cancel));
    }

    private StatusResponse<Uid> getPaymentStatusNotFound(Cancel cancel) {
        logger.warn(NOT_FOUND_PAYMENT_STATUS_FROM_LOG_FORMAT, cancel.getOriginUid());
        return getNotFoundResponse(cancel);
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

    private StatusResponse<Uid> getBadRequestStatusResponse(Cancel cancel, PaymentStatus paymentStatus) {
        return StatusResponse.<Uid>builder()
                .statusCode(ProvideStatusCode.BAD_REQUEST)
                .errMessage(String.format(BAD_REQUEST_REPONSE_FORMAT,
                        paymentStatus.getAmount(),
                        paymentStatus.getVat(),
                        cancel.getAmount().getAmount(),
                        cancel.getVat().getVat()))
                .build();
    }
}
