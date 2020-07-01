package com.teahyuk.payment.ap.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.Cancel;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import com.teahyuk.payment.ap.exception.BadRequestException;
import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CancelRequest {
    @JsonIgnore
    private final static String VAT_INVALID_FORMAT = "Vat must less then amount. %s, %s";

    private final int amount;
    private final Integer vat;

    @JsonIgnore
    public Cancel getCancel(Uid uid) throws BadRequestException {
        try {
            Amount amount = new Amount(this.amount);
            Vat vat = this.vat == null ? amount.createDefaultVat() : new Vat(this.vat);

            if (!amount.isValidVat(vat)) {
                throw new BadRequestException(String.format(VAT_INVALID_FORMAT, amount, vat));
            }

            return Cancel.builder()
                    .uid(uid)
                    .amount(amount)
                    .vat(vat)
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
