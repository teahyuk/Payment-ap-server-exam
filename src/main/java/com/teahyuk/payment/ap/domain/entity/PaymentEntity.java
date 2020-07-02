package com.teahyuk.payment.ap.domain.entity;

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
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class PaymentEntity extends TransactionInfo {
    @Column(length = 300)
    private String encryptedCardInfo;
    @Column
    private int installment;
    @OneToMany(mappedBy = "payment")
    private final List<CancelEntity> cancelEntities = new ArrayList<>();

    @Builder
    public PaymentEntity(Uid uid, CardInfo cardInfo, int installment, int vat, int amount) {
        super(uid, vat, amount);
        this.encryptedCardInfo = cardInfo.getEncryptedString(uid);
        this.installment = installment;
    }

    @Transient
    public CardInfo getCardInfo() {
        return CardInfo.ofEncryptedString(encryptedCardInfo, getUid());
    }
}
