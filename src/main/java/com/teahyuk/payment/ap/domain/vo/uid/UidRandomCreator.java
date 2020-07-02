package com.teahyuk.payment.ap.domain.vo.uid;

import com.teahyuk.payment.ap.domain.vo.card.CardNumber;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class UidRandomCreator {
    private final Random random = new Random();
    private CardNumber cardNumber;

    public UidRandomCreator cardNumber(CardNumber cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public Uid randomBuild() {
        String cardAndTimeMilli = longToBase64(Long.parseLong(cardNumber.getCardNumber())) + longToBase64(System.currentTimeMillis());
        int remainingLength = Uid.LENGTH - cardAndTimeMilli.length();
        if (remainingLength > 0) {
            cardAndTimeMilli += longToBase64(random.nextLong()).substring(0, remainingLength);
        }
        return new Uid(cardAndTimeMilli);
    }

    private String longToBase64(long v) {
        final char[] digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_+".toCharArray();
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
