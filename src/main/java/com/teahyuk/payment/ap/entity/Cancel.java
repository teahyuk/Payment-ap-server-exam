package com.teahyuk.payment.ap.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class Cancel extends PaymentInfo {
    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;

    @Builder
    public Cancel(String uid, String cardInfo, Integer amount, Integer vat, Payment payment) {
        super(uid, cardInfo, amount, vat);
        this.payment = payment;
    }
}
