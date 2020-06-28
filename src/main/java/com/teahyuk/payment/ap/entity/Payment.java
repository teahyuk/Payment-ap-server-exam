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
@Builder
@Entity
@Table(indexes = {@Index(columnList = "uid", unique = true)})
public class Payment {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 20, nullable = false)
    private String uid;

    @Column(length = 300)
    private String cardInfo;

    @Column(nullable = false)
    private Integer amount;

    @Column
    private Integer vat;

    @OneToMany(mappedBy = "payment")
    private List<Cancel> cancels = new ArrayList<>();
}
