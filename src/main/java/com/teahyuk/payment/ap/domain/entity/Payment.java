package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.Amount;
import com.teahyuk.payment.ap.domain.Installment;
import com.teahyuk.payment.ap.domain.Vat;
import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class Payment extends AmountInfo {

    @OneToMany(mappedBy = "payment")
    private final List<Cancel> cancels = new ArrayList<>();

    @Builder
    public Payment(Uid uid, Amount amount, Vat vat) {
        super(uid, amount, vat);
    }
}
