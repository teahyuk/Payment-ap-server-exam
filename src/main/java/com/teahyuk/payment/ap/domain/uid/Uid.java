package com.teahyuk.payment.ap.domain.uid;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class Uid {
    public static final int LENGTH = 20;
    private static final String INVALID_FORMAT = "Create uid error, uid must be has characters. uid=%s";

    @JsonValue
    private final String uid;

    public Uid(String uid) {
        this.uid = uid;
        validationCheck();
    }

    public static UidRandomCreator randomCreator() {
        return new UidRandomCreator();
    }

    private void validationCheck() {
        if (uid.length() != LENGTH) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, uid));
        }
    }
}
