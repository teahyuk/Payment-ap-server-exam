package com.teahyuk.payment.ap.auth.controller;

import com.teahyuk.payment.ap.dto.response.ProvideStatusCode;
import com.teahyuk.payment.ap.dto.response.ResponseErrorException;

public class SignInFailedException extends ResponseErrorException {
    private static final String FAILED_MSG = "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않습니다.";

    SignInFailedException(String message) {
        super(message, ProvideStatusCode.BAD_REQUEST, FAILED_MSG);
    }
}
