package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.*;
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
public class Cancel {
    private final Uid originUid;
    private final Amount amount;
    private Vat vat;

    public RequestToCompanyObject getRequestToObject(CardInfo cardInfo) {
        return RequestToCompanyObject.builder()
                .requestType(RequestType.CANCEL)
                .cardInfo(cardInfo)
                .installment(Installment.of(0))
                .amount(amount)
                .vat(vat)
                .originUid(originUid)
                .build();
    }

    public boolean isCancelable(RemainingPrice remainingPrice) {
        return this.amount.le(remainingPrice.getAmount()) &&
                this.vat.le(remainingPrice.getVat()) &&
                isValidRemainPrice(remainingPrice);
    }

    private boolean isValidRemainPrice(RemainingPrice remainingPrice) {
        return remainingPrice.getAmount().minus(amount).getAmount()
                >= remainingPrice.getVat().minus(vat).getVat();
    }

    public void settingVat(Vat remainVat) {
        if (this.vat == null) {
            this.vat = this.amount.createDefaultVat();
            if (!this.vat.le(remainVat)) {
                this.vat = remainVat;
            }
        }
    }

    public CancelEntity toEntity(PaymentEntity paymentEntity, Uid uid) {
        return CancelEntity.builder()
                .payment(paymentEntity)
                .uid(uid)
                .amount(amount)
                .vat(vat)
                .build();
    }
}
