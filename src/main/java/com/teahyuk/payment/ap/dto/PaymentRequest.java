package com.teahyuk.payment.ap.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.card.CardNumber;
import com.teahyuk.payment.ap.domain.card.Cvc;
import com.teahyuk.payment.ap.domain.card.Validity;
import com.teahyuk.payment.ap.domain.entity.StringData;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.RequestType;
import com.teahyuk.payment.ap.dto.card.company.StringDataRequest;
import com.teahyuk.payment.ap.util.CryptoException;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
public class PaymentRequest {
    //TODO validation 체크 가 지금 throwble 로 날라가서 400에러 처리가 너무 다보임
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
    public PaymentRequest(@NonNull CardNumber cardNumber, @NonNull Validity validity, @NonNull Cvc cvc, @NonNull Installment installment, @NonNull Amount amount, Vat vat) {
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
    public StringData getStringData() throws CryptoException {
        Uid uid = Uid.randomCreator()
                .cardNumber(cardNumber)
                .randomBuild();

        return StringData.builder()
                .string(StringDataRequest.builder()
                        .requestType(RequestType.PAYMENT)
                        .uid(uid)
                        .cardInfo(getCardInfo())
                        .installment(installment)
                        .amount(amount)
                        .vat(vat)
                        .build()
                        .getStringData())
                .uid(uid)
                .build();
    }
}
