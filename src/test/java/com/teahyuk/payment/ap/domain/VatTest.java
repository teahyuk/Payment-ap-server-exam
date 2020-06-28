package com.teahyuk.payment.ap.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VatTest {
    @ParameterizedTest
    @ValueSource(longs = {0, 9999999999L})
    void constructor(long vat) {
        assertThatCode(() -> new Vat(vat))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 10000000000L})
    void constructorError(long vat) {
        assertThatThrownBy(() -> new Vat(vat))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
