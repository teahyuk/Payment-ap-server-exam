package com.teahyuk.payment.ap.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class Payment extends PaymentInfo {

    @OneToMany(mappedBy = "payment")
    private List<Cancel> cancels = new ArrayList<>();

    @Builder
    public Payment(String uid, String cardInfo, Integer amount, Integer vat) {
        super(uid,cardInfo,amount,vat);
    }
}
