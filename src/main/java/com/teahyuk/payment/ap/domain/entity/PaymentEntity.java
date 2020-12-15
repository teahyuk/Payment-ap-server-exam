package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.Amount;
import com.teahyuk.payment.ap.domain.vo.Installment;
import com.teahyuk.payment.ap.domain.vo.RemainingPrice;
import com.teahyuk.payment.ap.domain.vo.Vat;
import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
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
@Table(name = "payment", indexes = {@Index(columnList = "uid", unique = true)})
public class PaymentEntity extends AmountInfo {
    @Column(length = 300)
    private String encryptedCardInfo;
    @Column
    private int installment;
    @OneToMany(mappedBy = "payment")
    private final List<CancelEntity> cancelEntities = new ArrayList<>();

    @Builder
    public PaymentEntity(Uid uid, CardInfo cardInfo, Installment installment, Amount amount, Vat vat) {
        super(uid, amount, vat);
        this.encryptedCardInfo = cardInfo.getEncryptedString(uid);
        this.installment = installment.getInstallment();
    }

    public CardInfo getCardInfo() {
        return CardInfo.ofEncryptedString(encryptedCardInfo, getUid());
    }

    public RemainingPrice getRemainingPrice() {
        return new RemainingPrice(new Amount(getAmount()), new Vat(getVat()));
    }
}
