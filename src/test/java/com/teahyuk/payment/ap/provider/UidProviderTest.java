package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.CardNumber;
import com.teahyuk.payment.ap.domain.CardNumberTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UidProviderTest {

    UidProvider uidProvider = new UidProvider();

    @Test
    @DisplayName("카드번호 최대 일 때의 테스트 를 확인 함으로써 제대로 uuid 를 뽑아 주는지 확인.")
    void test() {
        assertThat(uidProvider.makeUid(CardNumberTest.cardNumber1))
                .isNotEqualTo(uidProvider.makeUid(CardNumberTest.cardNumber1));
    }

}
