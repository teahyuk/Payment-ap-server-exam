package com.teahyuk.payment.ap.domain.vo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstallmentTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 2, 12})
    void constructor(int installment) {
        assertThatCode(() -> Installment.of(installment))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 1, 13})
    void constructorError(int installment) {
        assertThatThrownBy(() -> Installment.of(installment))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
