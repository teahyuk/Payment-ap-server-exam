package com.teahyuk.payment.ap.domain.uid;

import com.teahyuk.payment.ap.domain.card.CardNumberTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UidRandomCreatorTest {

    @Test
    void isDifferentEachBuild() {
        UidRandomCreator uidRandomCreator = Uid.randomCreator()
                .cardNumber(CardNumberTest.cardNumber1);
        assertThat(uidRandomCreator.randomBuild())
                .isNotEqualTo(uidRandomCreator.randomBuild());
    }

    @Test
    void canNotHasBlackChar() {
        UidRandomCreator uidRandomCreator = Uid.randomCreator()
                .cardNumber(CardNumberTest.cardNumber1);
        for (int i = 0; i < 10000; i++) {
            assertThat(uidRandomCreator.randomBuild().getUid())
                    .doesNotContain(" ");
        }
    }

}
