package com.teahyuk.payment.ap.domain.vo.card;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CvcTest {
    public static final Cvc cvc1 = new Cvc("000");

    @ParameterizedTest
    @ValueSource(strings = {"000", "900", "999"})
    void constructor(String cvc) {
        assertThatCode(() -> new Cvc(cvc))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0123", "8000", "se1", "+12"})
    void constructorError(String cvc) {
        assertThatThrownBy(() -> new Cvc(cvc))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
