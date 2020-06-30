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
public class CardCompanyDto {
    private final static String INVALID_FORMAT = "StringDataRequest build error. %s must not be null when requestType is %s";

    private final Uid uid;
    private final RequestType requestType;
    private final CardInfo cardInfo;
    private final Amount amount;
    private final Vat vat;
    private final Installment installment;
    private final Uid originUid;

    @Builder
    public CardCompanyDto(@NonNull RequestType requestType,
                          @NonNull Uid uid,
                          @NonNull CardInfo cardInfo,
                          @NonNull Amount amount,
                          @NonNull Vat vat,
                          Installment installment,
                          Uid originUid) {

        installment = isInstallmentNullOrCancelType(requestType, installment) ? Installment.of(0) : installment;

        this.uid = uid;
        this.requestType = requestType;
        this.cardInfo = cardInfo;
        this.amount = amount;
        this.vat = vat;
        this.installment = installment;
        this.originUid = originUid;
    }

    private boolean isInstallmentNullOrCancelType(@NonNull RequestType requestType, Installment installment) {
        return installment == null || RequestType.CANCEL.equals(requestType);
    }

    public boolean isValid() {
        if (RequestType.PAYMENT.equals(requestType)) {
            return originUid == null;
        }
        return originUid != null;
    }

    public static CardCompanyDto fromSerialized(String serializedString) {
//        return new CardCompanyDto(RequestType.valueOf(serializedString.substring(4,10).trim()),
//                new Uid(serializedString.substring(11,30)),
//                CardInfo.ofEncryptedString());
        return null;
    }

    public String getSerializedString() throws CryptoException {
        String payload = String.format("%-10s%-20s%-20s%02d%-4s%-3s%10s%010d%-20s%-300s%-47s",
                requestType.name(),
                uid.getUid(),
                cardInfo.getCardNumber().getCardNumber(),
                installment.getInstallment(),
                cardInfo.getValidity().getValidity(),
                cardInfo.getCvc().getCvc(),
                amount.getAmount(),
                vat.getVat(),
                originUid == null ? Strings.EMPTY : originUid.getUid(),
                cardInfo.getEncryptedString(uid),
                Strings.EMPTY);

        return String.format("%4s%s", payload.length(), payload);
    }
}
