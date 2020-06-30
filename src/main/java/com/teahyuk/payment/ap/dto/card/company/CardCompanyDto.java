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

        installment = isInstallmentNullOrCancelType(requestType, installment);

        this.uid = uid;
        this.requestType = requestType;
        this.cardInfo = cardInfo;
        this.amount = amount;
        this.vat = vat;
        this.installment = installment;
        this.originUid = originUid;
    }

    private Installment isInstallmentNullOrCancelType(@NonNull RequestType requestType, Installment installment) {
        if (installment == null || RequestType.CANCEL.equals(requestType)) {
            return Installment.of(0);
        }
        return installment;
    }

    public boolean isValid() {
        if (RequestType.PAYMENT.equals(requestType)) {
            return originUid == null;
        }
        return originUid != null;
    }

    public static CardCompanyDto fromSerialized(String serializedString) throws CryptoException {
        Uid uid = new Uid(serializedString.substring(14, 34));
        String originUid = serializedString.substring(83, 103).trim();
        return new CardCompanyDto(RequestType.valueOf(subString(serializedString, 4, 14)),
                new Uid(serializedString.substring(14, 34)),
                CardInfo.ofEncryptedString(subString(serializedString, 103, 403), uid),
                new Amount(Integer.parseInt(subString(serializedString, 63, 73))),
                new Vat(Integer.parseInt(subString(serializedString, 73, 83))),
                Installment.of(Integer.parseInt(subString(serializedString, 54, 56))),
                originUid.isEmpty() ? null : new Uid(originUid));
    }

    private static String subString(String serializedString, int startIdx, int endIdx) {
        return serializedString.substring(startIdx, endIdx).trim();
    }

    public String getSerializedString() {
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
