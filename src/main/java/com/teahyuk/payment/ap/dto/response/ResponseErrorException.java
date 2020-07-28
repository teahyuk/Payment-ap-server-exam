package com.teahyuk.payment.ap.dto.response;

public class ResponseErrorException extends Exception {
    private final ProvideStatusCode provideStatusCode;

    public ResponseErrorException(String msg, ProvideStatusCode provideStatusCode) {
        super(msg);
        this.provideStatusCode = provideStatusCode;
    }

    public int getStatusCode() {
        return provideStatusCode.getCode();
    }
}
