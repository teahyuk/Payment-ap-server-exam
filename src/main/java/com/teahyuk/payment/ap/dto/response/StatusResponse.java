package com.teahyuk.payment.ap.dto.response;

import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public class StatusResponse<T> {
    private final T responseData;
    private final ProvideStatusCode statusCode;
    private final String message;

    public ResponseEntity<?> responseEntity(){
        if(statusCode.isSuccess()){
            return ResponseEntity.status(statusCode.getCode())
                    .body(responseData);
        }
        return ResponseEntity.status(statusCode.getCode())
                .body(message);
    }
}
