package com.teahyuk.payment.ap.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AES256CryptoTest {

    AES256Crypto aes256Crypto = new AES256Crypto();

    @Test
    void testEncryptAndDecrypt() throws Exception {
        String plainText = "Hello, World!";
        String key = "secret key";
        String encrypted = aes256Crypto.encrypt(plainText, key);
        assertThat(encrypted)
                .isNotEqualTo(plainText);
        assertThat(aes256Crypto.decrypt(encrypted, key))
                .isEqualTo(plainText);
    }
}
