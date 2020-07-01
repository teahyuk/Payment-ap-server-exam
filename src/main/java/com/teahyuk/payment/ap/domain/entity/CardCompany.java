package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.card.CardInfo;
import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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

    @Transient
    public CardInfo getCardInfo() {
        return CardInfo.ofEncryptedString(string.substring(103, 403).trim(), getUid());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCompany that = (CardCompany) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}
