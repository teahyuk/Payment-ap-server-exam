package com.teahyuk.payment.ap.repository;

import com.teahyuk.payment.ap.domain.entity.StringData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StringDataRepository extends JpaRepository<StringData, Long> {
    Optional<StringData> findByUid(String uid);
}
