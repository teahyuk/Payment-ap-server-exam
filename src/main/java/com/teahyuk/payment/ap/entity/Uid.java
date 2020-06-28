package com.teahyuk.payment.ap.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Uid {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, nullable = false)
    private String uid;

    public Uid(String uid) {
        this.uid = uid;
    }
}
