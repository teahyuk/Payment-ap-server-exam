package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
abstract class AmountInfo extends EntityUid {
    @Column
    private int amount;
    @Column
    private int vat;

    public AmountInfo(Uid uid, Amount amount, Vat vat) {
        super(uid);
        this.vat = vat.getVat();
        this.amount = amount.getAmount();
    }
}
