package com.teahyuk.payment.ap.entity;

import com.teahyuk.payment.ap.domain.Uid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class StringData extends EntityId {
    @Column(length = 450)
    private String string;

    @Builder
    public StringData(Uid uid, String string) {
        super(uid);
        this.string = string;
    }
}
