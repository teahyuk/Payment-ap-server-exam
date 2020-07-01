package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class PaymentStatus extends EntityUid {
    @Column
    private int amount;
    @Column
    private int vat;

    @Builder
    public PaymentStatus(Uid uid, Amount amount, Vat vat) {
        super(uid);
        this.amount = amount.getAmount();
        this.vat = vat.getVat();
    }
}