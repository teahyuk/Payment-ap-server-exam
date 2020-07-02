package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.CardCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardCompanyRepository extends JpaRepository<CardCompany, Long> {
    Optional<CardCompany> findByUid(String uid);
}
