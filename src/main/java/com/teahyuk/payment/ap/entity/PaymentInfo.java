package com.teahyuk.payment.ap.entity;

import com.teahyuk.payment.ap.domain.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class PaymentInfo extends EntityId {
    @Column(length = 300, nullable = false)
    private String cardInfo;

    @Column
    private int amount;

    @Column
    private Integer vat;

    public PaymentInfo(Uid uid, String cardInfo, int amount, Integer vat) {
        super(uid);
        this.cardInfo = cardInfo;
        this.amount = amount;
        this.vat = vat;
    }
}