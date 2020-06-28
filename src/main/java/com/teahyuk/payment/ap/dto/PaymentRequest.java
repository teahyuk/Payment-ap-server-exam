package com.teahyuk.payment.ap.dto;

import com.teahyuk.payment.ap.domain.*;
import lombok.*;

@Getter
@Builder
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
