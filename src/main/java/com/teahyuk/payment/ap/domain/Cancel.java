package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
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

    public boolean settingVatAndCheckCancelable(PaymentStatus paymentStatus) {
        if (this.vat == null) {
            settingVat(paymentStatus.getVat());
        }

        return this.amount.le(paymentStatus.getAmount()) &&
                this.vat.le(paymentStatus.getVat()) &&
                isRemainingAmountIsGeVat(paymentStatus);
    }

    private void settingVat(int originVat) {
        this.vat = this.amount.createDefaultVat();
        if (!this.vat.le(originVat)) {
            this.vat = new Vat(originVat);
        }
    }

    private boolean isRemainingAmountIsGeVat(PaymentStatus paymentStatus) {
        return this.amount.getRemaining(paymentStatus.getAmount())
                >= this.vat.getRemaining(paymentStatus.getVat());
    }

    public void cancel(PaymentStatus paymentStatus) {
        if (!settingVatAndCheckCancelable(paymentStatus)) {
            throw new IllegalStateException("can not cancel payment status!");
        }

        paymentStatus.setAmount(this.amount.getRemaining(paymentStatus.getAmount()));
        paymentStatus.setVat(this.vat.getRemaining(paymentStatus.getVat()));
    }
}
