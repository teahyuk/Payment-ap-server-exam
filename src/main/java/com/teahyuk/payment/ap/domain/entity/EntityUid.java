package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class EntityUid {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = Uid.LENGTH, nullable = false)
    private String uid;

    public EntityUid(Uid uid) {
        this.uid = uid.getUid();
    }
}
