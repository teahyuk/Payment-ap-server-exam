package com.teahyuk.payment.ap.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class StringData {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, nullable = false)
    private String uid;

    @Column(length = 450)
    private String cardInfo;
}
