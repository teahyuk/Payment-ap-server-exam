package com.teahyuk.payment.ap.dto.request;

import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.uid.UidTest;
import com.teahyuk.payment.ap.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CancelRequestTest {

    @Test
    void buildDefaultVatTest() throws BadRequestException {
        CancelRequest paymentDefaultVatRequest = CancelRequest.builder()
                .amount(1100)
                .build();

        assertThat(paymentDefaultVatRequest.getCancel(UidTest.createTestUid("444")).getVat())
                .isEqualTo(new Vat(100));
    }

}
