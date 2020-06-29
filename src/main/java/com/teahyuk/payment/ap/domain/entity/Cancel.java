package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class Cancel extends AmountInfo {
    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @Builder
    public Cancel(Uid uid, String cardInfo, Amount amount, Vat vat, Payment payment) {
        super(uid, amount, vat);
        this.payment = payment;
    }
}
