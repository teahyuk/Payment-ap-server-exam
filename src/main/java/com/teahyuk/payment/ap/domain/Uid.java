package com.teahyuk.payment.ap.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Uid {
    private static final String INVALID_FORMAT = "Create uid error, uid must be has characters. uid=%s";
    private final String uid;

    public Uid(String uid) {
        this.uid = uid;
        validationCheck();
    }

    private void validationCheck() {
        if (uid.length()!=20) {
            throw new IllegalArgumentException(String.format(INVALID_FORMAT, uid));
        }
    }

    @JsonValue
    public String getUid() {
        return uid;
    }
}
