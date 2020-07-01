package com.teahyuk.payment.ap.dto.response;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class UidResponse {
    private final Uid uid;
}
