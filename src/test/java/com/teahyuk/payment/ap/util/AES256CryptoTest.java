package com.teahyuk.payment.ap.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AES256CryptoTest {
    private final String plainText = "Hello, World!";
    private final String key = "secret key";

    private String encrypted1;
    private String encrypted2;


    @BeforeEach
    void setting() {
        encrypted1 = AES256Crypto.encrypt(plainText, key);
        encrypted2 = AES256Crypto.encrypt(plainText, key);
    }

    @Test
    @DisplayName("매번 salt 치기 때문에 같은 key 로 암호화 할 때 마다 다른 값이 나온다.")
    void encryptTest() {
        assertThat(encrypted1)
                .isNotEqualTo(encrypted2);
    }

    @Test
    @DisplayName("매번 다른 암호화 된 값 이지만 같은 key 로 복호화가 가능 하다.")
    void decryptTest() {
        assertThat(plainText)
                .isEqualTo(AES256Crypto.decrypt(encrypted1, key));
        assertThat(plainText)
                .isEqualTo(AES256Crypto.decrypt(encrypted2, key));
    }
}
