package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.ResponseErrorException;

public class SignUpFailedException extends ResponseErrorException {
    private static final String FAILED_MSG = "이미 등록된 사용자 ID 입니다.";

    SignUpFailedException(String message) {
        super(message, ProvideStatusCode.CONFLICT, FAILED_MSG);
    }
}
