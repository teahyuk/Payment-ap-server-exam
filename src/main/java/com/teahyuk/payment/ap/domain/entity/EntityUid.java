package com.teahyuk.payment.ap.domain.entity;

import com.teahyuk.payment.ap.domain.vo.uid.Uid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
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
