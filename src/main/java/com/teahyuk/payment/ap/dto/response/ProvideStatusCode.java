package com.teahyuk.payment.ap.dto.response;

import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
public enum ProvideStatusCode {
    SUCCESS(200, true),
    BAD_REQUEST(400, false),
    NOT_FOUND(404, false),
    CONFLICT(409, false);

    private final int code;
    private final boolean isSuccess;

    ProvideStatusCode(int code, boolean isSuccess) {
        this.code = code;
        this.isSuccess = isSuccess;
    }
}
