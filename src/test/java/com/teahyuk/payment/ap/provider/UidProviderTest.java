package com.teahyuk.payment.ap.provider;

import com.teahyuk.payment.ap.domain.CardNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UidProviderTest {

    UidProvider uidProvider = new UidProvider();

    @Test
    @DisplayName("카드번호 최대 일 때의 테스트 를 확인 함으로써 제대로 uuid 를 뽑아 주는지 확인.")
    void test() {
        assertThat(uidProvider.makeUid(new CardNumber("9999999999999999")))
                .isNotEqualTo(uidProvider.makeUid(new CardNumber("9999999999999999")));
    }

}
