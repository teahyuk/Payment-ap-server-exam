package com.teahyuk.payment.ap.util;

public interface CryptoUtil {
    String encrypt(String msg, String key) throws CryptoException;
    String decrypt(String msg, String key) throws CryptoException;
}
