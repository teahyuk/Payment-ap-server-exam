package com.teahyuk.payment.ap.domain;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EqualsAndHashCode
class CvcTest {
    @ParameterizedTest
    @ValueSource(strings = {"000", "900", "999"})
    void constructor(String cvc) {
        assertThatCode(() -> new Cvc(cvc))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0123", "8000"})
    void constructorError(String cvc) {
        assertThatThrownBy(() -> new Cvc(cvc))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
