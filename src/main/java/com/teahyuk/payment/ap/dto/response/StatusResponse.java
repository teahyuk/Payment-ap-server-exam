package com.teahyuk.payment.ap.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.function.Function;

@Getter
public class StatusResponse<T> {
    private final T data;
    private final ProvideStatusCode statusCode;
    private final String errMessage;

    @Builder
    public StatusResponse(T data, ProvideStatusCode statusCode, String errMessage) {
        this.data = data;
        this.statusCode = statusCode;
        this.errMessage = errMessage;
    }

    public <R> StatusResponse<R> map(Function<T, R> bodyMapper) {
        return StatusResponse.<R>builder()
                .data(bodyMapper.apply(data))
                .statusCode(statusCode)
                .errMessage(errMessage)
                .build();
    }


    public ResponseEntity<?> responseEntity() {
        if (statusCode.isSuccess()) {
            return ResponseEntity.status(statusCode.getCode())
                    .body(this.data);
        }

        return ResponseEntity.status(statusCode.getCode())
                .body(this.errMessage);
    }

    public boolean isSuccess() {
        return statusCode.isSuccess();
    }
}
