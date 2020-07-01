package com.teahyuk.payment.ap.domain;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RequestType;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.dto.card.company.RequestToCompanyObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Cancel {
    private final Uid originUid;
    private final Amount amount;
    private final Vat vat;

    public RequestToCompanyObject getRequestToObject(CardInfo cardInfo) {
        return RequestToCompanyObject.builder()
                .requestType(RequestType.CANCEL)
                .cardInfo(cardInfo)
                .installment(Installment.of(0))
                .amount(amount)
                .vat(vat)
                .originUid(originUid)
                .build();
    }
}
