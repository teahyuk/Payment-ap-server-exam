package com.teahyuk.payment.ap.domain.uid;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UidTest {
    public static Uid createTestUid(String uid) {
        return new Uid(String.format("%20s", uid));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345678901234567890", "abcdefghijklmnopqrst"})
    void constructor(String uid) {
        assertThatCode(() -> new Uid(uid))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890123456789", "123456789012345678901"})
    void constructorError(String uid) {
        assertThatThrownBy(() -> new Uid(uid))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
