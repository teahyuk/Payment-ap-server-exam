package com.teahyuk.payment.ap.exception;

import com.teahyuk.payment.ap.dto.response.ResponseErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    //유효성 검사 실패 시 발생 하는 예외를 처리
    @ExceptionHandler(value = {BadRequestException.class, MethodArgumentTypeMismatchException.class})
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

    //응답 에러 코드 예외 처리
    @ExceptionHandler(value = {ResponseErrorException.class})
    protected ResponseEntity<?> handleErrorStatus(ResponseErrorException exception) {
        logger.error(exception);
        return ResponseEntity.status(exception.getStatusCode())
                .body(exception.getMessage());
    }
}
