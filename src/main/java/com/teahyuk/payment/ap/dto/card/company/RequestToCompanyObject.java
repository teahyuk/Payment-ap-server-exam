package com.teahyuk.payment.ap.dto.card.company;

import com.teahyuk.payment.ap.domain.entity.CardCompany;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.logging.log4j.util.Strings;

@Builder
@EqualsAndHashCode
@ToString
public class RequestToCompanyObject {
    private final RequestType requestType;
    private final CardInfo cardInfo;
    private final Amount amount;
    private final Vat vat;
    private final Installment installment;
    private final Uid originUid;

    public String serialize(Uid uid) {
        try {
            if (RequestType.PAYMENT.equals(requestType)) {
                if (originUid != null) {
                    throw new CardCompanySerializeException("payment request must not have originUid origin Uid=%s", originUid);
                }
            }

            if (RequestType.CANCEL.equals(requestType)) {
                if (originUid == null) {
                    throw new CardCompanySerializeException("cancel request must have originUid originUid=null");
                }
            }

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
        } catch (Exception e) {
            throw new CardCompanySerializeException(e.getMessage());
        }
    }

    public Uid createUid() {
        return Uid.randomCreator()
                .cardNumber(cardInfo.getCardNumber())
                .randomBuild();
    }

    public CardCompany toEntity(Uid uid) {
        return CardCompany.builder()
                .string(serialize(uid))
                .uid(uid)
                .build();
    }
}
