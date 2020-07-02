package com.teahyuk.payment.ap.dto.response;

import com.teahyuk.payment.ap.domain.entity.CancelEntity;
import com.teahyuk.payment.ap.domain.entity.PaymentEntity;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {
    private final String uid;
    private final String cardNumber;
    private final Validity validity;
    private final Cvc cvc;
    private final RequestType requestType;
    private final int amount;
    private final int vat;

    public static PaymentResponse fromPayment(PaymentEntity paymentEntity) {
        CardInfo cardInfo = paymentEntity.getCardInfo();
        return PaymentResponse.builder()
                .uid(paymentEntity.getUid())
                .cardNumber(maskingCardNumber(cardInfo.getCardNumber()))
                .validity(cardInfo.getValidity())
                .cvc(cardInfo.getCvc())
                .requestType(RequestType.PAYMENT)
                .amount(paymentEntity.getAmount())
                .vat(paymentEntity.getVat())
                .build();
    }

    public static PaymentResponse fromCancel(CancelEntity cancelEntity) {
        CardInfo cardInfo = cancelEntity.getPayment().getCardInfo();
        return PaymentResponse.builder()
                .uid(cancelEntity.getUid())
                .cardNumber(maskingCardNumber(cardInfo.getCardNumber()))
                .validity(cardInfo.getValidity())
                .cvc(cardInfo.getCvc())
                .requestType(RequestType.CANCEL)
                .amount(cancelEntity.getAmount())
                .vat(cancelEntity.getVat())
                .build();
    }

    private static String maskingCardNumber(CardNumber cardNumber) {
        String number = cardNumber.getCardNumber();
        return String.format("******%s***", number.substring(6, number.length() - 3));
    }
}
