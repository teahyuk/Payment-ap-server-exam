package com.teahyuk.payment.ap.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.card.CardNumber;
import com.teahyuk.payment.ap.domain.card.Cvc;
import com.teahyuk.payment.ap.domain.card.Validity;
import com.teahyuk.payment.ap.domain.entity.Payment;
import com.teahyuk.payment.ap.domain.uid.Uid;
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
}
