package com.teahyuk.payment.ap.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VatTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1000000000})
    void constructor(int vat) {
        assertThatCode(() -> new Vat(vat))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1000000001})
    void constructorError(int vat) {
        assertThatThrownBy(() -> new Vat(vat))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
