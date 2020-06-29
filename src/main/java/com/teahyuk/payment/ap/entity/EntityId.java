package com.teahyuk.payment.ap.entity;

import com.teahyuk.payment.ap.domain.uid.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class EntityId {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private Uid uid;

    public EntityId(Uid uid) {
        this.uid = uid;
    }
}
