package com.teahyuk.payment.ap.domain;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@EqualsAndHashCode
class AmountTest {
    @ParameterizedTest
    @ValueSource(longs = {100, 9999999999L})
    void constructor(long amount) {
        assertThatCode(() -> new Amount(amount))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {99, 10000000000L})
    void constructorError(long amount) {
        assertThatThrownBy(() -> new Amount(amount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,91",
            "20000,1818"
    })
    void getVatDefault(long amount, long expectVat) {
        assertThat(new Amount(amount).getVat())
                .isEqualTo(new Vat(expectVat));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,0",
            "20000,20000"
    })
    void getVat(long amount, long vat) {
        assertThat(new Amount(amount).getVat(vat))
                .isEqualTo(new Vat(vat));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,-1",
            "20000,20001"
    })
    void getVatThrowable(long amount, long vat) {
        assertThatThrownBy(() -> new Amount(amount).getVat(vat))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
