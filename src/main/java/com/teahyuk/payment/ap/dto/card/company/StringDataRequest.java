package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardInfo;
import com.teahyuk.payment.ap.domain.uid.Uid;
import com.teahyuk.payment.ap.util.CryptoException;
import lombok.*;
import org.apache.logging.log4j.util.Strings;

@ToString
@Getter
@EqualsAndHashCode
public class StringDataRequest {
    private final static String INVALID_FORMAT = "StringDataRequest build error. %s must not be null when requestType is %s";

    private final Uid uid;
    private final String stringData;

    @Builder
    public StringDataRequest(@NonNull RequestType requestType,
                             @NonNull Uid uid,
                             @NonNull CardInfo cardInfo,
                             @NonNull Amount amount,
                             @NonNull Vat vat,
                             Installment installment,
                             String originUid) throws CryptoException {
        validCheckNullable(RequestType.PAYMENT, requestType, installment, "installment");
        validCheckNullable(RequestType.CANCEL, requestType, originUid, "originUid");

        installment = installment == null ? Installment.of(0) : installment;
        originUid = originUid == null ? Strings.EMPTY : originUid;

        this.uid = uid;
        this.stringData = makeStringData(requestType, uid, cardInfo, amount, vat, installment, originUid, cardInfo.getEncryptedString(uid));
    }

    private void validCheckNullable(RequestType expectedType, RequestType requestType, Object nullableObject,
                                    String fieldName) {
        if (requestType.equals(expectedType) && nullableObject == null) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, fieldName, requestType));
        }
    }

    private String makeStringData(RequestType requestType, Uid uid, CardInfo cardInfo, Amount amount, Vat vat, Installment installment, String originUid, String encryptedCardInfo) {
        String payload = String.format("%-10s%-20s%-20s%02d%-4s%-3s%10s%010d%-20s%-300s%-47s",
                requestType.name(),
                uid.getUid(),
                cardInfo.getCardNumber().getCardNumber(),
                installment.getInstallment(),
                cardInfo.getValidity().getValidity(),
                cardInfo.getCvc().getCvc(),
                amount.getAmount(),
                vat.getVat(),
                originUid,
                encryptedCardInfo,
                Strings.EMPTY);

        return String.format("%4s%s", payload.length(), payload);
    }
}
