package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.dto.card.company.CardCompanyDto;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
public class Payment {
    @NonNull
    private final CardNumber cardNumber;
    @NonNull
    private final Validity validity;
    @NonNull
    private final Cvc cvc;
    @NonNull
    private final Installment installment;
    @NonNull
    private final Amount amount;
    private final Vat vat;

    @Builder
    public Payment(@NonNull CardNumber cardNumber, @NonNull Validity validity, @NonNull Cvc cvc, @NonNull Installment installment, @NonNull Amount amount, Vat vat) {
        this.cardNumber = cardNumber;
        this.validity = validity;
        this.cvc = cvc;
        this.installment = installment;
        this.amount = amount;
        this.vat = vat == null ? amount.createDefaultVat() : vat;
    }

    private CardInfo getCardInfo() {
        return CardInfo.builder()
                .cardNumber(cardNumber)
                .cvc(cvc)
                .validity(validity)
                .build();
    }

    @JsonIgnore
    public CardCompanyDto getCardCompanyDto() {
        return CardCompanyDto.builder()
                .requestType(RequestType.PAYMENT)
                .cardInfo(getCardInfo())
                .installment(installment)
                .amount(amount)
                .vat(vat)
                .build();
    }
}
