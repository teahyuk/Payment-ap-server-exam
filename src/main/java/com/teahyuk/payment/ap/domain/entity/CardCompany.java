package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class CardCompany extends EntityUid {
    @Column(length = 450)
    private String string;

    @Builder
    public CardCompany(Uid uid, String string) {
        super(uid);
        this.string = string;
    }
}
