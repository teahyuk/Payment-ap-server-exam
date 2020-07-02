package com.teahyuk.payment.ap.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class CancelEntity extends AmountInfo {
    @ManyToOne
    private PaymentEntity payment;

    @Builder
    public CancelEntity(PaymentEntity payment, int vat, int amount) {
        this.payment = payment;

    }
}
