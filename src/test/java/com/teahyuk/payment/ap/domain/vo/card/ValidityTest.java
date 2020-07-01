package com.teahyuk.payment.ap.domain.vo.card;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.*;

public class ValidityTest {
    public static final Validity thisMonthValidity = new Validity(getYearMonthString(YearMonth.now()));
    public static final Validity lastMonthValidity = new Validity(getYearMonthString(YearMonth.now().minusMonths(1)));

    private static String getYearMonthString(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern("MMyy"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0119", "1211"})
    void constructor(String validity) {
        assertThatCode(() -> new Validity(validity));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0019", "1311"})
    void validationCheck(String validity) {
        assertThatThrownBy(() -> new Validity(validity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0520,true",
            "1299,false"
    })
    void isExpired(String validity, boolean expectExpired) {
        assertThat(new Validity(validity).isExpired())
                .isEqualTo(expectExpired);
    }

}
