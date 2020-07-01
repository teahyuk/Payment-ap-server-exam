package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Payment {
    private final CardInfo cardInfo;
    private final Installment installment;
    private final Amount amount;
    private final Vat vat;

    public RequestToCompanyObject getRequestToCompanyObject() {
        return RequestToCompanyObject.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(cardInfo)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build();
    }

    public PaymentStatus getPaymentStatus(Uid uid) {
        return PaymentStatus.builder()
                .uid(uid)
                .amount(amount)
                .vat(vat)
                .build();
    }
}
