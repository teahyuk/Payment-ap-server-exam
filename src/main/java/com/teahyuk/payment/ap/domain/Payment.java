package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.entity.PaymentStatus;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
public class Payment {
    private final CardInfo cardInfo;
    private final Installment installment;
    private final Amount amount;
    private final Vat vat;

    @Builder
    public Payment(@NonNull CardNumber cardNumber, @NonNull Validity validity, @NonNull Cvc cvc,
                   @NonNull Installment installment, @NonNull Amount amount, @NonNull Vat vat) {
        this.cardInfo = CardInfo.builder()
                .cardNumber(cardNumber)
                .cvc(cvc)
                .validity(validity)
                .build();
        this.installment = installment;
        this.amount = amount;
        this.vat = vat;
    }

    @JsonIgnore
    public CardCompanyDto getCardCompanyDto() {
        return CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(cardInfo)
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build();
    }

    @JsonIgnore
    public PaymentStatus getPaymentStatus(Uid uid) {
        return PaymentStatus.builder()
                .uid(uid)
                .amount(amount)
                .vat(vat)
                .build();
    }
}
