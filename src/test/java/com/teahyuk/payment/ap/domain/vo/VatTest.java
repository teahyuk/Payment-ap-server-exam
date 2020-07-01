package com.teahyuk.payment.ap.domain.vo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

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

    @ParameterizedTest
    @CsvSource(value = {
            "100,1000,true",
            "100,100,true",
            "101,100,false"
    })
    void leTest(int vat, int thenVat, boolean le) {
        assertThat(new Vat(vat).le(thenVat))
                .isEqualTo(le);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "100,1000,900",
            "100,100,0"
    })
    void getRemainingTest(int vat, int thenVat, int result) {
        assertThat(new Vat(vat).getRemaining(thenVat))
                .isEqualTo(result);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "102,101",
            "106,105"
    })
    void getRemainingExceptionTest(int vat, int thenVat) {
        assertThatThrownBy(()->new Vat(vat).getRemaining(thenVat))
                .isInstanceOf(ArithmeticException.class);
    }
}
