package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Vat;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Tuple;
import java.math.BigInteger;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode
public class RemainingPrice {
    private final Amount amount;
    private final Vat vat;

    public static RemainingPrice of(Tuple tuple) {
        return new RemainingPrice(new Amount(getInt(tuple, "amount")),
                new Vat(getInt(tuple,"vat")));
    }

    private static int getInt(Tuple tuple, String key) {
        return ((BigInteger) tuple.get(key)).intValue();
    }
}
