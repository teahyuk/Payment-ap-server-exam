package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class AmountInfo extends EntityUid {
    @Column
    private int amount;

    @Column
    private int vat;

    public AmountInfo(Uid uid, Amount amount, Vat vat) {
        super(uid);
        this.amount = amount.getAmount();
        this.vat = vat.getVat();
    }
}
