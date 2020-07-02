package com.teahyuk.payment.ap.domain.vo;

import lombok.*;

import javax.persistence.Tuple;
import java.math.BigInteger;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
@ToString
public class RemainingPrice {
    private final Amount amount;
    private final Vat vat;

    public RemainingPrice subtract(Tuple tuple) {
        return new RemainingPrice(
                amount.minus(new Amount(getInt(tuple, "amount"))),
                vat.minus(new Vat(getInt(tuple, "vat"))));
    }

    private int getInt(Tuple tuple, String key) {
        BigInteger value = ((BigInteger) tuple.get(key));
        return value == null ? 0 : value.intValue();
    }
}
