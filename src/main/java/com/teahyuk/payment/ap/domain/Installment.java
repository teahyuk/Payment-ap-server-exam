package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@EqualsAndHashCode
public class Installment {
    private final static String INVALID_FORMAT = "Create Installment error, Installment must be 0,2~12. installment=%d";
    private final static Map<Integer, Installment> INSTALLMENT_MAP = Stream.of(0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
            .collect(Collectors.toMap(Function.identity(), Installment::new));

    @JsonValue
    private final int installment;

    private Installment(int installment) {
        this.installment = installment;
    }

    public static Installment of(int installment) {
        validationCheck(installment);
        return INSTALLMENT_MAP.get(installment);
    }

    private static void validationCheck(int installment) {
        if (!INSTALLMENT_MAP.containsKey(installment)) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, installment));
        }
    }

    @Override
    public String toString() {
        return String.format("Installment(installment=%2d)", installment);
    }
}
