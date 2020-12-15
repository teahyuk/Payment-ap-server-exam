package com.teahyuk.payment.ap.dto.response;

public class ResponseErrorException extends Exception {
    private final ProvideStatusCode provideStatusCode;
    private final String responseMessage;

    public ResponseErrorException(String message, ProvideStatusCode provideStatusCode) {
        super(message);
        this.provideStatusCode = provideStatusCode;
        this.responseMessage = message;
    }

    public ResponseErrorException(String logMessage, ProvideStatusCode provideStatusCode, String responseMessage) {
        super(logMessage);
        this.provideStatusCode = provideStatusCode;
        this.responseMessage = responseMessage;
    }

    public int getStatusCode() {
        return provideStatusCode.getCode();
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
