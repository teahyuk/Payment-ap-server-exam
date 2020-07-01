package com.teahyuk.payment.ap.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.card.CardNumber;
import com.teahyuk.payment.ap.domain.card.Cvc;
import com.teahyuk.payment.ap.domain.card.Validity;
import com.teahyuk.payment.ap.exception.BadRequestException;
import lombok.*;

@Getter
@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class PaymentRequestDto {
    private final static String VAT_INVALID_FORMAT = "Vat must less then amount. %s, %s";
    private final static String VALIDITY_INVALID_FORMAT = "Card is expired. %s";

    private final String cardNumber;
    private final String validity;
    private final String cvc;
    private final int installment;
    private final int amount;
    private final Integer vat;

    @JsonIgnore
    public PaymentRequest getPaymentRequest() throws BadRequestException {
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

            return PaymentRequest.builder()
                    .cardNumber(new CardNumber(cardNumber))
                    .validity(validity)
                    .cvc(new Cvc(cvc))
                    .amount(amount)
                    .vat(vat)
                    .installment(Installment.of(installment))
                    .build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
