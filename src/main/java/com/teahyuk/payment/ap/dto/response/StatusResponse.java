package com.teahyuk.payment.ap.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class StatusResponse<T> {
    private final T data;
    private final ProvideStatusCode statusCode;
    private final String errMessage;

    @JsonIgnore
    public ResponseEntity<StatusResponse<T>> responseEntity() {
        return ResponseEntity.status(statusCode.getCode())
                .body(this);
    }
}
