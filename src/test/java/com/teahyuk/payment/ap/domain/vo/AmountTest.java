package com.teahyuk.payment.ap.domain.vo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

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
            "1000,0,true",
            "20000,20000,true",
            "20000,20001,false",
    })
    void isValidVat(int amount, int vat, boolean expectValid) {
        assertThat(new Amount(amount).isValidVat(new Vat(vat)))
                .isEqualTo(expectValid);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1100,100",
            "20000,1818"
    })
    void createVatTest(int amount, int expectDefaultVat) {
        assertThat(new Amount(amount).createDefaultVat().getVat())
                .isEqualTo(expectDefaultVat);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "100,1000,true",
            "100,100,true",
            "101,100,false"
    })
    void leTest(int amount, int thenAmount, boolean le) {
        assertThat(new Amount(amount).le(new Amount(thenAmount)))
                .isEqualTo(le);
    }
}
