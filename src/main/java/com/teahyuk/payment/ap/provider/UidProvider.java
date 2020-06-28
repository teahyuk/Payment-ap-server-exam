package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.CardNumber;
import com.teahyuk.payment.ap.domain.Uid;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UidProvider {

    private final Random random = new Random();

    public Uid makeUid(CardNumber cardNumber) {
        String cardAndTimeMilli = longToBase64(Long.parseLong(cardNumber.getCardNumber())) + longToBase64(System.currentTimeMillis());
        int remainingLength = 20 - cardAndTimeMilli.length();
        if (remainingLength != 0) {
            cardAndTimeMilli += longToBase64(random.nextLong()).substring(0, remainingLength);
        }
        return new Uid(cardAndTimeMilli);
    }

    private static String longToBase64(long v) {
        final char[] digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 /".toCharArray();
        int shift = 6;
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        long number = v;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }

}
