package com.teahyuk.payment.ap.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.Payment;
import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import com.teahyuk.payment.ap.domain.vo.card.Cvc;
import com.teahyuk.payment.ap.domain.vo.card.Validity;
import com.teahyuk.payment.ap.exception.BadRequestException;
import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PaymentRequest {
    @JsonIgnore
    private final static String VAT_INVALID_FORMAT = "Vat must less then amount. %s, %s";
    @JsonIgnore
    private final static String VALIDITY_INVALID_FORMAT = "Card is expired. %s";

    private final String cardNumber;
    private final String validity;
    private final String cvc;
    private final Integer installment;
    private final Integer amount;
    private final Integer vat;

    @JsonIgnore
    public Payment getPayment() throws BadRequestException {
        try {
            Amount amount = new Amount(this.amount);
            Vat vat = this.vat == null ? amount.createDefaultVat() : new Vat(this.vat);
            Validity validity = new Validity(this.validity);

            if (!amount.isValidVat(vat)) {
                throw new BadRequestException(String.format(VAT_INVALID_FORMAT, amount, vat));
            }

            if (validity.isExpired()) {
                throw new BadRequestException(String.format(VALIDITY_INVALID_FORMAT, validity));
            }

            return Payment.builder()
                    .cardInfo(CardInfo.builder()
                            .cardNumber(new CardNumber(cardNumber))
                            .validity(validity)
                            .cvc(new Cvc(cvc))
                            .build())
                    .amount(amount)
                    .vat(vat)
                    .installment(Installment.of(installment))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
