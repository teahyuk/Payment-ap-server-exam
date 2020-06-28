package com.teahyuk.payment.ap.domain;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@EqualsAndHashCode
class AmountTest {
    @ParameterizedTest
    @ValueSource(ints = {100, 1000000000})
    void constructor(int amount) {
        assertThatCode(() -> new Amount(amount))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {99, 1000000001})
    void constructorError(int amount) {
        assertThatThrownBy(() -> new Amount(amount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,91",
            "20000,1818"
    })
    void getVatDefault(int amount, int expectVat) {
        assertThat(new Amount(amount).getVat())
                .isEqualTo(new Vat(expectVat));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,0",
            "20000,20000"
    })
    void getVat(int amount, int vat) {
        assertThat(new Amount(amount).getVat(vat))
                .isEqualTo(new Vat(vat));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1000,-1",
            "20000,20001"
    })
    void getVatThrowable(int amount, int vat) {
        assertThatThrownBy(() -> new Amount(amount).getVat(vat))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
