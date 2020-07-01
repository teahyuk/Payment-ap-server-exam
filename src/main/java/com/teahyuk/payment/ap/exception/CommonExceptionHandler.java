package com.teahyuk.payment.ap.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    //유효성 검사 실패 시 발생 하는 예외를 처리
    @ExceptionHandler(value = {BadRequestException.class, MethodArgumentTypeMismatchException.class})
    @ResponseBody
    protected ResponseEntity<?> handleInvalid(Exception exception) {
        logger.error(findIllegalArgumentException(exception));
        return ResponseEntity.badRequest()
                .body(findIllegalArgumentException(exception).getMessage());
    }

    private Throwable findIllegalArgumentException(Exception e) {
        Throwable throwable = e;
        while (throwable != null && !(throwable instanceof IllegalArgumentException)) {
            throwable = throwable.getCause();
        }
        return throwable == null ? e : throwable;
    }
}
