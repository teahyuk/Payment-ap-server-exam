package com.teahyuk.payment.ap.domain;

import lombok.EqualsAndHashCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@EqualsAndHashCode
class CardNumberTest {
    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "1234567890123456","0001234567"})
    void constructor(String cardNumber) {
        assertThatCode(() -> new CardNumber(cardNumber))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"000123456", "01234567890123456"})
    void constructorError(String cardNumber) {
        assertThatThrownBy(() -> new CardNumber(cardNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
