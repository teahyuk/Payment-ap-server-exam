package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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

    public PaymentEntity toEntity(Uid uid) {
        return PaymentEntity.builder()
                .uid(uid)
                .cardInfo(cardInfo)
                .amount(amount)
                .vat(vat)
                .installment(installment)
                .build();
    }
}
