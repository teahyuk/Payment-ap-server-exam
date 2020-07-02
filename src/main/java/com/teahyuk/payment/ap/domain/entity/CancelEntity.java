package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cancel", indexes = {@Index(columnList = "uid", unique = true)})
public class CancelEntity extends AmountInfo {
    @ManyToOne
    @JoinColumn(name = "payment", nullable = false)
    private PaymentEntity payment;

    @Builder
    public CancelEntity(Uid uid, Amount amount, Vat vat, PaymentEntity payment) {
        super(uid, amount, vat);
        this.payment = payment;
    }
}
