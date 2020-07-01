package com.teahyuk.payment.ap.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    //유효성 검사 실패 시 발생 하는 예외를 처리
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseBody
    protected ResponseEntity<?> handleException(BadRequestException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getMessage());
    }
}
