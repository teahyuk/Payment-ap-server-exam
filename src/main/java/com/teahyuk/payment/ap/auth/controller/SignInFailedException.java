package com.teahyuk.payment.ap.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class SignInFailedException extends HttpStatusCodeException {
    private static final String FAILED_MSG = "계정이 존재하지 않거나 이메일 또는 비밀번호가 정확하지 않습니다.";

    protected SignInFailedException() {
        super(HttpStatus.BAD_REQUEST, FAILED_MSG);
    }
}
